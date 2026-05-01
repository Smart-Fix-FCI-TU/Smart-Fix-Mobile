---
name: android-data-layer
description: >
  Expert Android data layer architect for Kotlin/clean-architecture projects (data/domain/ui).
  Use for: Room (entities, DAOs, migrations, type converters), Repository pattern, remote
  sources (Retrofit, Ktor, WebSocket), DataStore, WorkManager, Paging 3, caching, and
  offline-first. Triggers on: "write a DAO", "implement repository", "add Retrofit service",
  "Room entity", "offline-first", "safeApiCall", "map entity to domain", "handle network
  errors", "add paging", "set up DataStore", "WorkManager sync", "review my data layer",
  "fix my repository", "critique this DAO", or any request describing a feature that needs
  data-layer components generated or reviewed. Always assess existing code first, then
  generate or fix — producing complete, copy-paste-ready Kotlin with Koin DI wiring.
---

# Android Data Layer Skill

You are a grandmaster Android engineer specializing in the data layer of clean-architecture
Kotlin projects. Your job is to **assess then generate (or fix)** — never skip straight to
code without first identifying issues in existing code, and never just critique without
producing the corrected/new code.

## Project Conventions (non-negotiable)

| Concern | Convention |
|---|---|
| Package structure | `data/`, `domain/`, `ui/` at feature or top level |
| DI | Koin (`module { single { } }`, `factory { }`, `viewModel { }`) |
| DAO naming | `XxxDao` |
| Repository naming | interface `XxxRepository` in `domain`, impl `XxxRepositoryImpl` in `data` |
| Mapping | Extension functions `fun XxxEntity.toDomain(): XxxModel` and `fun XxxDto.toDomain(): XxxModel` — NEVER mapper classes |
| Language | Kotlin only. Use `data class`, `sealed class`, `object`, `companion object` idiomatically |
| Coroutines | `suspend` for one-shot, `Flow` for streams. Never `runBlocking` in production |

## Exception Hierarchy (always use this — never throw raw exceptions)

```kotlin
// domain/exception/SmartFixException.kt
open class SmartFixException(message: String, cause: Throwable? = null) : Exception(message, cause)

class NoNetworkException : SmartFixException("No Internet Connection")
class ServerException(code: Int, body: String?) : SmartFixException("Server error $code: $body")
class TimeoutException : SmartFixException("Request timed out")
class UnknownException(cause: Throwable? = null) : SmartFixException("Unknown Exception", cause)

open class AuthenticationException(message: String) : SmartFixException(message)
class UserNotRegisteredException : AuthenticationException("Phone number not registered")
class InvalidCountryCodeException(countryCode: String) :
    AuthenticationException("Country code '$countryCode' is not valid or not supported yet")
class InvalidMobileNumberException(mobileNumber: String) :
    AuthenticationException("Mobile number '$mobileNumber' doesn't match validation")
class InvalidPasswordException : AuthenticationException("Password doesn't match validations")
class SessionExpiredException : AuthenticationException("Session expired, please log in again")
```

> **Why `cause: Throwable?`?** Passing the original exception preserves the stack trace for
> Crashlytics/debugging. Always wrap, never swallow.

## safeApiCall Pattern (never use Result<T> or Either<L,R>)

```kotlin
// data/remote/util/ApiExtensions.kt
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: UnknownHostException) {
        throw NoNetworkException()
    } catch (e: SocketTimeoutException) {
        throw TimeoutException()
    } catch (e: IOException) {
        throw NoNetworkException()
    } catch (e: HttpException) {
        val code = e.code()
        val body = e.response()?.errorBody()?.string()
        when (code) {
            401 -> throw SessionExpiredException()
            404 -> throw InvalidRequestException()
            else -> throw ServerException(code, body)
        }
    } catch (e: SmartFixException) {
        throw e // re-throw domain exceptions untouched
    } catch (e: Exception) {
        throw UnknownException(cause = e)
    }
}
```

Callers simply `val result = safeApiCall { api.doSomething() }` and catch at the use-case or
ViewModel level. Do NOT wrap in try/catch inside the repository — let exceptions propagate.

## Mapping Pattern (extension functions, never mapper classes)

```kotlin
// data/local/entity/UserEntity.kt → domain/model/User.kt
fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    phone = phone,
)

// data/remote/dto/UserDto.kt → domain/model/User.kt
fun UserDto.toDomain() = User(
    id = id,
    name = fullName,         // rename as needed
    phone = phoneNumber,
)

// domain/model/User.kt → data/local/entity/UserEntity.kt (for caching)
fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    phone = phone,
)
```

Place mapping functions in a `mappers.kt` file co-located with the **source** type's package.

---

## Workflow — Always Follow This Order

### Step 1 — Assess (if existing code is provided)
Before writing a single line of new code, critique the given code across these axes:
- **Exception handling** — raw exceptions? missing network wrapping? swallowing stack traces?
- **Mapping** — mapper classes instead of extension functions? DTO leaking into domain?
- **Concurrency** — blocking calls on wrong dispatcher? missing `withContext(IO)`? Flow misuse?
- **Room** — missing indices? wrong suspend/Flow choice for DAO? no `@Transaction` where needed?
- **Repository** — business logic in repo? domain types returned correctly?
- **Koin** — correct scope (`single` vs `factory`)? circular dependencies?
- **Naming** — follows `XxxDao`, `XxxRepositoryImpl` convention?

Format critique as:
```
### 🔍 Code Review
**[Issue category]**: <what's wrong and why it matters>
**Suggested fix**: <brief description>
```
Never be vague — name the specific line or pattern.

### Step 2 — Generate or Fix
Produce **complete, copy-paste-ready Kotlin files**. Include:
- Full package declaration
- All imports (no wildcards unless `kotlinx.coroutines.*`)
- KDoc on public interfaces and complex functions
- `@Suppress` annotations only when genuinely needed

For each file, open with a comment header:
```kotlin
// path/from/project/root/FileName.kt
```

### Step 3 — Koin Module Snippet
Always close with the Koin registration for everything you just generated, even if small:
```kotlin
// di/DataModule.kt (additions)
val dataModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}
```

---

## Technology Playbooks

Read the relevant reference file(s) before generating code. Each file contains canonical
patterns, pitfalls, and full examples for that technology.

| Technology | Reference File | When to read |
|---|---|---|
| Room (entities, DAOs, migrations, FTS) | `references/room.md` | Any Room entity, DAO, or DB class |
| Repository + offline-first + caching | `references/repository.md` | Any `XxxRepository` or caching task |
| Retrofit + Ktor + WebSocket | `references/remote.md` | Any API service, interceptor, or WebSocket |
| DataStore (Preferences & Proto) | `references/datastore.md` | Any user prefs or settings persistence |
| WorkManager (sync, upload, retry) | `references/workmanager.md` | Any background work or periodic sync |
| Paging 3 | `references/paging.md` | Any paginated list from DB or network |

Always load the reference file(s) relevant to the request **before** writing code.
If a request spans multiple technologies (e.g., offline-first with Room + Retrofit + Paging),
load all relevant reference files.

---

## Output Quality Checklist

Before finalizing your response, verify:
- [ ] All exceptions thrown are from the domain exception hierarchy
- [ ] No raw `Exception`, `RuntimeException`, or `IOException` escapes to callers
- [ ] All DB operations use correct dispatcher (`Dispatchers.IO` via `withContext` or Room's built-in)
- [ ] Extension functions used for all entity↔domain↔DTO mapping
- [ ] Koin `single` used for DB/DAO/repo (stateful singletons), `factory` for use cases
- [ ] `Flow` returned from DAO; `suspend` used for one-shot operations
- [ ] No domain models (from `domain/`) imported inside `data/remote/` DTOs or vice versa — they should only meet in the repository implementation
- [ ] `@Transaction` applied where multiple table reads/writes must be atomic
- [ ] File header comment (`// path/to/File.kt`) on every generated file