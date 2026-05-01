# DataStore Reference — Preferences & Proto

## Table of Contents
1. Preferences DataStore (key-value)
2. Proto DataStore (typed, recommended for complex state)
3. Exposing via Repository pattern
4. Migration from SharedPreferences
5. Koin wiring

---

## 1. Preferences DataStore

```kotlin
// data/local/datastore/UserPreferencesDataStore.kt

// Keys
private object Keys {
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
    val USER_ID = stringPreferencesKey("user_id")
    val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    val THEME = stringPreferencesKey("theme")                     // store enum name as String
}

class UserPreferencesDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "user_preferences")

    // Observe a value (Flow)
    val authToken: Flow<String?> = context.dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { it[Keys.AUTH_TOKEN] }

    val isOnboardingDone: Flow<Boolean> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[Keys.ONBOARDING_DONE] ?: false }

    // Write
    suspend fun setAuthToken(token: String) {
        context.dataStore.edit { it[Keys.AUTH_TOKEN] = token }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { it.remove(Keys.AUTH_TOKEN) }
    }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }

    // Read one-shot (not for UI — use Flow instead)
    suspend fun getAuthTokenOnce(): String? =
        context.dataStore.data.first()[Keys.AUTH_TOKEN]
}
```

**Rules:**
- Always handle `IOException` in `.catch { }` — DataStore throws it on disk errors
- Never call `.first()` from the main thread; use `withContext(Dispatchers.IO)` if needed
- Expose `Flow`, not `suspend get()` — let collectors decide when to read

---

## 2. Proto DataStore (preferred for structured state)

Define `.proto` in `app/src/main/proto/`:

```proto
// user_preferences.proto
syntax = "proto3";
option java_package = "com.example.app.data.local.datastore";
option java_multiple_files = true;

message UserPreferences {
  string auth_token = 1;
  string user_id = 2;
  bool onboarding_done = 3;
  AppTheme theme = 4;

  enum AppTheme {
    SYSTEM = 0;
    LIGHT = 1;
    DARK = 2;
  }
}
```

```kotlin
// data/local/datastore/UserPreferencesSerializer.kt
object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        try { UserPreferences.parseFrom(input) }
        catch (e: InvalidProtocolBufferException) { throw CorruptionException("Cannot read proto", e) }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        t.writeTo(output)
}

// data/local/datastore/UserPreferencesDataStore.kt (Proto version)
class UserPreferencesDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<UserPreferences>
        by dataStore(fileName = "user_prefs.pb", serializer = UserPreferencesSerializer)

    val preferences: Flow<UserPreferences> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(UserPreferences.getDefaultInstance()) else throw e }

    suspend fun setAuthToken(token: String) {
        context.dataStore.updateData { it.toBuilder().setAuthToken(token).build() }
    }

    suspend fun setTheme(theme: UserPreferences.AppTheme) {
        context.dataStore.updateData { it.toBuilder().setTheme(theme).build() }
    }
}
```

---

## 3. Exposing via Repository Pattern

```kotlin
// domain/repository/PreferencesRepository.kt
interface PreferencesRepository {
    val authToken: Flow<String?>
    val isOnboardingDone: Flow<Boolean>
    suspend fun saveAuthToken(token: String)
    suspend fun clearSession()
}

// data/repository/PreferencesRepositoryImpl.kt
class PreferencesRepositoryImpl(
    private val dataStore: UserPreferencesDataStore,
) : PreferencesRepository {
    override val authToken = dataStore.authToken
    override val isOnboardingDone = dataStore.isOnboardingDone
    override suspend fun saveAuthToken(token: String) = dataStore.setAuthToken(token)
    override suspend fun clearSession() = dataStore.clearAuthToken()
}
```

---

## 4. Migration from SharedPreferences

```kotlin
private val SP_MIGRATION = SharedPreferencesMigration(context, "legacy_prefs") { spData, current ->
    current.toBuilder()
        .apply {
            if (spData.contains("auth_token")) authToken = spData.getString("auth_token", "")
        }
        .build()
}

// Add to DataStore builder
by dataStore(fileName = "user_prefs.pb", serializer = UserPreferencesSerializer,
             produceMigrations = { listOf(SP_MIGRATION) })
```

---

## 5. Koin Wiring

```kotlin
val dataStoreModule = module {
    single { UserPreferencesDataStore(androidContext()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}
```