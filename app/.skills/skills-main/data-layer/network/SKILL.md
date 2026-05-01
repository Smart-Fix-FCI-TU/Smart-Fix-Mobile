---
name: network-layer-grandmaster
description: >
  Grandmaster-level Android network layer specialist for the SmartFix project. Use
  this skill whenever API client setup, HTTP configuration, authentication headers,
  token refresh, request interceptors, timeouts, TLS/certificate pinning, serialization,
  or any networking concern is involved. Also trigger when reviewing network code that
  has: hardcoded URLs, missing error handling, no auth headers, insecure HTTP, improper
  coroutine usage, or no token refresh strategy. Trigger phrases: "set up networking",
  "API client", "authentication", "token refresh", "interceptor", "Retrofit", "Ktor",
  "OkHttp", "timeout", "API error", "offline support", "base URL", "headers".
  Covers BOTH Retrofit and Ktor Client as two parallel tracks — includes a decision
  guide for choosing between them. Koin DI. SmartFixResult integration.
---

# Network Layer Grandmaster — SmartFix

Designs and implements the complete SmartFix network layer. Covers both Retrofit and
Ktor Client as first-class options. Integrates natively with `SmartFixResult<T>` and
the `SmartFixException` hierarchy from the error-handling-grandmaster skill.

## Project context

| Concern | Choice |
|---|---|
| DI | Koin |
| Serialization | kotlinx.serialization |
| Auth storage | DataStore (encrypted) |
| Error model | `SmartFixException` via `toSmartFixException()` mapper |
| Module structure | Single module — `di/networkModule.kt` |

---

## Step 1 — Choose your HTTP client

Read `references/client-decision-guide.md` before recommending or building either client.
If the user hasn't decided yet, walk them through the guide and get a decision first.

**Quick summary:**

| | Retrofit | Ktor Client |
|---|---|---|
| Best for | Standard REST APIs, team familiarity, fast setup | Multiplatform, WebSockets, fine-grained control |
| Coroutine support | Via suspend functions (excellent) | Native (excellent) |
| Interceptors | OkHttp interceptors (mature ecosystem) | Plugins/features (newer, less ecosystem) |
| Boilerplate | Low (annotations) | Medium (manual request building) |
| SmartFix recommendation | ✅ Default choice for pure Android | ✅ If KMP is planned |

---

## Step 2 — Auth architecture (same for both clients)

```
DataStore<Preferences>  ←→  TokenRepository
        ↓
AuthInterceptor / AuthPlugin   ← adds Bearer token to every request
        ↓
TokenRefreshHandler            ← intercepts 401, refreshes, replays
        ↓
Koin networkModule             ← wires everything
```

Token refresh strategy:
1. Detect 401 response.
2. Acquire a Mutex lock — prevents concurrent refresh storms.
3. Compare current stored token with the token used in the failed request.
   If they differ, another coroutine already refreshed — replay with the new token.
4. If same: call refresh endpoint using a **separate** unauthenticated client.
5. Save new tokens to DataStore.
6. Release Mutex, replay original request.
7. If refresh fails: emit `SessionExpiredException` → trigger logout nav event in VM.

Read `references/retrofit-setup.md` for full Retrofit implementation.
Read `references/ktor-setup.md` for full Ktor Client implementation.
Read `references/token-repository.md` for the shared `TokenRepository` (same for both).

---

## Core invariants (both clients)

**Base URL never hardcoded.**
Always from `BuildConfig.BASE_URL`. One value per build variant.

**All HTTP calls are suspend functions.**
```kotlin
// Retrofit: suspend fun getUser(...): UserDto
// Ktor: suspend fun getUser(...): UserDto  (via manual request)
// Never: Call<T>, Observable<T>, or blocking calls
```

**HTTP exceptions are caught at the repository boundary — never above.**
Both clients throw on non-2xx. The repository catches via `.catchAsFailure()` or
`.catch { emit(SmartFixResult.fromThrowable(it)) }`.

**Tokens stored in DataStore — never SharedPreferences.**
SharedPreferences is not encrypted by default. DataStore with `EncryptedDataStore`
or `androidx.security.crypto` is required.

**Logging stripped in release builds.**
Both clients support conditional logging. Always guard with `BuildConfig.DEBUG`.

**Certificate pinning in production.**
Retrofit (OkHttp): `CertificatePinner`. Ktor: `HttpsURLConnection` config or OkHttp engine.

---

## Koin networkModule structure

```kotlin
// di/networkModule.kt
val networkModule = module {
    // Shared: TokenRepository, DataStore
    single { TokenRepository(dataStore = get()) }

    // Pick ONE block below based on chosen client:

    // ── Retrofit track ──────────────────────────────────────────────────────
    single { provideOkHttpClient(tokenRepository = get()) }
    single { provideRetrofit(okHttpClient = get(), json = get()) }

    // ── Ktor track ───────────────────────────────────────────────────────────
    single { provideKtorClient(tokenRepository = get()) }

    // Feature API services (add one line per feature)
    single { get<Retrofit>().create(TechnicianApiService::class.java) }  // Retrofit
    // OR
    single { TechnicianKtorService(client = get(), baseUrl = BuildConfig.BASE_URL) }  // Ktor
}
```

---

## Security checklist

- [ ] `BuildConfig.BASE_URL` — no hardcoded URLs anywhere in code
- [ ] `network_security_config.xml` — cleartext HTTP disabled in production
- [ ] Certificate pinning configured for the production API host
- [ ] Tokens in DataStore (encrypted), never SharedPreferences
- [ ] Authorization header redacted from logs
- [ ] Response bodies with PII not logged in release
- [ ] `@SerialName` on every DTO field (no reflection-based name matching)
- [ ] ProGuard/R8 rules retain DTO classes and Retrofit service interfaces

---

## Code review triggers — flag immediately

```kotlin
// ❌ Hardcoded base URL
private const val BASE_URL = "https://api.smartfix.com/"

// ❌ HttpException / ResponseException leaking to ViewModel
viewModelScope.launch { try { api.call() } catch (e: HttpException) { ... } }

// ❌ Token in SharedPreferences
sharedPrefs.putString("access_token", token)

// ❌ Logging in release without guard
addInterceptor(HttpLoggingInterceptor())   // no BuildConfig.DEBUG check

// ❌ No timeout configuration (uses client defaults — too long for mobile)
OkHttpClient.Builder().build()
HttpClient(Android) { }  // no timeout plugin

// ❌ Mutable global auth state
companion object { var token: String = "" }

// ❌ Using GlobalScope for network calls
GlobalScope.launch { apiService.getUser(id) }

// ❌ CancellationException swallowed in catch block
catch (e: Exception) { /* catches cancellation too */ }
```