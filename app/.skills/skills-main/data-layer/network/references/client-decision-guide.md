# Client Decision Guide — Retrofit vs Ktor

Use this to help the SmartFix team decide before building the network layer.
Ask these questions in order. The first decisive answer wins.

---

## Question 1: Is Kotlin Multiplatform (KMP) planned?

**YES** → **Use Ktor Client.**
Retrofit depends on OkHttp which is JVM-only. Ktor Client is multiplatform-native
and will work in a future KMP module without migration.

**NO / Not sure** → continue to Q2.

---

## Question 2: Does the app need WebSockets or Server-Sent Events?

**YES** → **Use Ktor Client.**
Retrofit does not support WebSockets. Ktor has first-class WebSocket support via
`webSocket { }` blocks using the same client and auth configuration.

**NO** → continue to Q3.

---

## Question 3: Does the team have existing Retrofit experience?

**YES** → **Use Retrofit.**
Retrofit's annotation-based API is widely known, has a vast ecosystem of examples,
and produces the least boilerplate for standard REST. The team will be productive
immediately.

**NO / Mixed** → continue to Q4.

---

## Question 4: How many distinct API endpoints does the app have?

**< 20 endpoints** → Either is fine. Prefer Retrofit for lower boilerplate.
**≥ 20 endpoints** → Retrofit scales better. Each endpoint is one annotated interface
method. Ktor requires manual request building per call — more verbose at scale.

---

## Side-by-side: same endpoint in both

```kotlin
// Retrofit
interface TechnicianApiService {
    @GET("technicians/{id}")
    suspend fun getTechnician(@Path("id") id: String): TechnicianDto
}

// Ktor
class TechnicianKtorService(private val client: HttpClient, private val baseUrl: String) {
    suspend fun getTechnician(id: String): TechnicianDto =
        client.get("$baseUrl/technicians/$id").body()
}
```

Retrofit: 2 lines. Ktor: 4 lines. At 30 endpoints this difference is significant.

---

## SmartFix recommendation

**Default: Retrofit** unless KMP or WebSockets are on the roadmap.

Both integrate equally well with:
- Koin DI
- kotlinx.serialization
- `SmartFixResult<T>` error handling
- Token refresh pattern
- Coroutines / Flow

The token refresh and auth patterns are architecturally identical. Switching later
requires rewriting API service files but NOT repositories, use cases, or ViewModels —
the domain and presentation layers are fully isolated from the HTTP client choice.