# Remote Data Sources — Retrofit, Ktor, WebSocket

## Table of Contents
1. Retrofit setup
2. API service interface
3. RemoteDataSource class (wraps service + safeApiCall)
4. OkHttp interceptors (auth, logging)
5. Ktor (alternative to Retrofit)
6. WebSocket with Ktor
7. DTO patterns
8. Koin wiring

---

## 1. Retrofit Setup

```kotlin
// data/remote/network/RetrofitFactory.kt
object RetrofitFactory {

    fun create(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))  // kotlinx.serialization
            .build()

    fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
            })
            .build()
}
```

---

## 2. API Service Interface

```kotlin
// data/remote/api/UserApiService.kt
interface UserApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20,
    ): UsersResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto

    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest): UserDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body request: UpdateUserRequest): UserDto

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)                  // 204 No Content → Unit
}
```

---

## 3. RemoteDataSource (the right abstraction)

The remote data source isolates Retrofit/Ktor from the repository. It converts HTTP calls into
domain exceptions via `safeApiCall`.

```kotlin
// data/remote/source/UserRemoteDataSource.kt
interface UserRemoteDataSource {
    suspend fun fetchUsers(): List<UserDto>
    suspend fun fetchUserById(id: String): UserDto
    suspend fun createUser(request: CreateUserRequest): UserDto
    suspend fun updateUser(id: String, request: UpdateUserRequest): UserDto
    suspend fun deleteUser(id: String)
}

// data/remote/source/UserRemoteDataSourceImpl.kt
class UserRemoteDataSourceImpl(
    private val api: UserApiService,
) : UserRemoteDataSource {

    override suspend fun fetchUsers(): List<UserDto> =
        safeApiCall { api.getUsers(page = 1).users }

    override suspend fun fetchUserById(id: String): UserDto =
        safeApiCall { api.getUserById(id) }

    override suspend fun createUser(request: CreateUserRequest): UserDto =
        safeApiCall { api.createUser(request) }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): UserDto =
        safeApiCall { api.updateUser(id, request) }

    override suspend fun deleteUser(id: String) =
        safeApiCall { api.deleteUser(id) }
}
```

---

## 4. OkHttp Interceptors

### Auth Interceptor

```kotlin
// data/remote/interceptor/AuthInterceptor.kt
class AuthInterceptor(
    private val tokenProvider: TokenProvider,   // e.g., from DataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getTokenBlocking()   // blocking OK inside Interceptor
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
```

### Token Refresh Interceptor (Authenticator)

```kotlin
// data/remote/interceptor/TokenAuthenticator.kt
class TokenAuthenticator(
    private val tokenProvider: TokenProvider,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        val newToken = tokenProvider.refreshBlocking() ?: return null  // null = give up
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }
}
```

---

## 5. DTO Patterns

```kotlin
// data/remote/dto/UserDto.kt
@Serializable
data class UserDto(
    val id: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("created_at") val createdAt: String,     // ISO-8601 string from API
)

// Mapping: DTO → Domain (extension function, co-located with DTO)
fun UserDto.toDomain() = User(
    id = id,
    name = fullName,
    phone = phoneNumber,
    createdAt = Instant.parse(createdAt),
)

// Mapping: DTO → Entity (for caching)
fun UserDto.toEntity() = UserEntity(
    id = id,
    name = fullName,
    phone = phoneNumber,
    createdAt = Instant.parse(createdAt).toEpochMilli(),
    isSynced = true,
)
```

---

## 6. Ktor Client (alternative to Retrofit)

```kotlin
// data/remote/network/KtorClientFactory.kt
fun provideKtorClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
    }
    install(Logging) {
        level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
        header("Accept", "application/json")
    }
}

// Usage in data source
class UserRemoteDataSourceImpl(private val client: HttpClient) : UserRemoteDataSource {
    override suspend fun fetchUsers(): List<UserDto> = safeApiCall {
        client.get("$BASE_URL/users").body()
    }
}
```

For Ktor, expand `safeApiCall` to also catch `ResponseException`:

```kotlin
} catch (e: ResponseException) {
    val code = e.response.status.value
    when (code) {
        401 -> throw SessionExpiredException()
        else -> throw ServerException(code, e.message)
    }
}
```

---

## 7. WebSocket with Ktor

```kotlin
// data/remote/source/ChatRemoteDataSource.kt
class ChatRemoteDataSourceImpl(
    private val client: HttpClient,
) : ChatRemoteDataSource {

    override fun observeMessages(roomId: String): Flow<ChatMessageDto> = callbackFlow {
        val session = client.webSocketSession("$WS_URL/rooms/$roomId")

        launch {
            try {
                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        val dto = Json.decodeFromString<ChatMessageDto>(frame.readText())
                        trySend(dto)
                    }
                }
            } catch (e: Exception) {
                close(e)
            }
        }

        awaitClose { session.cancel() }
    }.catch { e ->
        // Convert to domain exception
        when (e) {
            is UnknownHostException -> throw NoNetworkException()
            is SmartFixException -> throw e
            else -> throw UnknownException(cause = e)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun sendMessage(roomId: String, text: String) = safeApiCall {
        // One-shot REST endpoint is simpler than sending over WS for most use cases
        client.post("$BASE_URL/rooms/$roomId/messages") {
            setBody(SendMessageRequest(text))
        }
    }
}
```

---

## 8. Koin Wiring

```kotlin
val networkModule = module {
    single { AuthInterceptor(get()) }
    single { RetrofitFactory.okHttpClient(get()) }
    single { RetrofitFactory.create(BuildConfig.BASE_URL, get()) }
    single { get<Retrofit>().create(UserApiService::class.java) }
    // Ktor (if used instead)
    single { provideKtorClient() }
}

val dataModule = module {
    includes(networkModule)
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
}
```