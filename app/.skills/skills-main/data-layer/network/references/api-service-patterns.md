# API Service Patterns

## Retrofit service conventions

```kotlin
interface UserProfileApiService {

    // Simple GET
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserProfileDto

    // GET with query params
    @GET("users")
    suspend fun searchUsers(
        @Query("q")       query: String,
        @Query("page")    page: Int = 1,
        @Query("limit")   limit: Int = 20,
    ): PagedResponseDto<UserProfileDto>

    // POST with body
    @POST("users/{id}/profile")
    suspend fun updateProfile(
        @Path("id")  id: String,
        @Body        body: UpdateProfileRequestDto,
    ): UserProfileDto

    // PATCH
    @PATCH("users/{id}")
    suspend fun patchUser(
        @Path("id") id: String,
        @Body       body: PatchUserRequestDto,
    ): UserProfileDto

    // DELETE (returns Unit — Retrofit handles 204 No Content)
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Unit

    // File upload
    @Multipart
    @POST("users/{id}/avatar")
    suspend fun uploadAvatar(
        @Path("id")    id: String,
        @Part         avatar: MultipartBody.Part,
    ): AvatarResponseDto

    // Custom headers per request
    @GET("admin/stats")
    @Headers("X-Admin: true")
    suspend fun getAdminStats(): AdminStatsDto
}
```

---

## DTO conventions

```kotlin
@Serializable
data class UserProfileDto(
    // All fields nullable — the API can always change
    @SerialName("id")          val id: String?,
    @SerialName("name")        val displayName: String?,
    @SerialName("email")       val email: String?,
    @SerialName("avatar_url")  val avatarUrl: String?,
    @SerialName("created_at")  val createdAt: String?,   // ISO-8601 string
    @SerialName("meta")        val meta: MetaDto?,
)

@Serializable
data class MetaDto(
    @SerialName("role")         val role: String?,
    @SerialName("verified")     val verified: Boolean?,
)

// Paged responses
@Serializable
data class PagedResponseDto<T>(
    @SerialName("data")         val data: List<T> = emptyList(),
    @SerialName("total")        val total: Int?,
    @SerialName("page")         val page: Int?,
    @SerialName("limit")        val limit: Int?,
    @SerialName("has_more")     val hasMore: Boolean?,
)

// Request bodies (never reuse DTOs as request bodies)
@Serializable
data class UpdateProfileRequestDto(
    @SerialName("name")         val displayName: String,
    @SerialName("email")        val email: String,
)
```

---

## Providing API services via Hilt

Each feature's API service is provided in that feature's Hilt module:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UserProfileModule {

    @Provides
    @Singleton
    fun provideUserProfileApiService(retrofit: Retrofit): UserProfileApiService =
        retrofit.create(UserProfileApiService::class.java)

    @Binds @Singleton
    abstract fun bindUserProfileRepository(
        impl: UserProfileRepositoryImpl
    ): UserProfileRepository
}
```

Do NOT put all `retrofit.create()` calls in `NetworkModule`. Each feature owns its service.

---

## File upload helper

```kotlin
fun File.toMultipartBody(partName: String = "file"): MultipartBody.Part {
    val mediaType = when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png"         -> "image/png"
        "pdf"         -> "application/pdf"
        else          -> "application/octet-stream"
    }.toMediaType()
    return MultipartBody.Part.createFormData(partName, name, asRequestBody(mediaType))
}
```