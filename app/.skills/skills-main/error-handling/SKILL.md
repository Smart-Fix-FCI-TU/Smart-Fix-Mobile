---
name: error-handling-grandmaster
description: >
  Grandmaster-level Android error handling specialist for the SmartFix project. Use
  this skill whenever error handling, exception design, error propagation, result types,
  retry logic, error UI states, or failure flows are involved. Also trigger when code
  shows raw try/catch in ViewModels, exceptions thrown from repositories, nullable
  returns used to signal failure, or inconsistent error handling across layers. Trigger
  phrases: "handle errors", "something went wrong", "show error to user", "exception",
  "retry", "error state", "catch", "failure", "network error", "validation error",
  "authentication error". This skill owns the full SmartFix error lifecycle:
  SmartFixException hierarchy → SmartFixResult<T> wrapper → network/local mapping →
  ViewModel translation → Compose error UI → recovery actions.
---

# Error Handling Grandmaster — SmartFix

Owns the complete error handling system for SmartFix. Extends and standardizes the
existing `SmartFixException` hierarchy, wraps it in a type-safe `SmartFixResult<T>`
flow, and provides patterns for every layer from data boundary to Compose UI.

## Core philosophy

**Exceptions are domain citizens, not control flow.**
- `SmartFixException` subclasses model *what went wrong* in domain terms.
- `SmartFixResult<T>` is the carrier — it makes failure visible in the type system.
- Raw exceptions are caught **only** at the data boundary (RepositoryImpl).
- The ViewModel never sees a raw `Throwable` — only typed `SmartFixResult.Failure`.
- Every failure has: a typed exception, a user-readable message, and a recovery action.

---

## The SmartFixException hierarchy (yours + recommended extensions)

Read `references/exception-hierarchy.md` for the full annotated hierarchy with:
- Your existing exceptions (preserved exactly)
- Recommended additions with rationale
- The `toRecoveryAction()` extension that maps each exception to a UI action
- The `fromThrowable()` factory that maps network/platform exceptions to your domain types

**Summary of recommended additions to your existing hierarchy:**

```
SmartFixException (existing)
├── NoNetworkException (existing)
├── InvalidRequestException (existing) → add field: String? for inline field errors
├── UnknownException (existing) → add cause: Throwable for debuggability
├── AuthenticationException (existing)
│   ├── UserNotRegisteredException (existing)
│   ├── InvalidCountryCodeException (existing)
│   ├── InvalidMobileNumberException (existing)
│   ├── InvalidPasswordException (existing)
│   └── SessionExpiredException [NEW] ← for 401 after token refresh fails
├── ServerException [NEW] ← for 5xx responses
│   └── data class with: code: Int, serverMessage: String?
└── LocalDataException [NEW] ← for Room/DataStore failures
    └── data class with: cause: Throwable
```

---

## SmartFixResult<T> — the result wrapper

```kotlin
sealed class SmartFixResult<out T> {
    data class Success<out T>(val data: T) : SmartFixResult<T>()
    data class Loading<out T>(val cached: T? = null) : SmartFixResult<T>()
    data class Failure<out T>(val exception: SmartFixException) : SmartFixResult<T>()
}
```

Read `references/result-type.md` for the complete implementation including:
- `map()`, `getOrNull()`, `onSuccess()`, `onFailure()` extensions
- `fromThrowable()` factory that maps any `Throwable` to `SmartFixException`
- Flow extension helpers

---

## Layer responsibilities

### Data layer — the ONLY place that catches raw exceptions

```kotlin
// RepositoryImpl: catch at the boundary, classify, emit
override fun getTechnician(id: String): Flow<SmartFixResult<Technician>> = flow {
    emit(SmartFixResult.Loading())
    val dto = apiService.getTechnician(id)  // may throw HttpException, IOException, etc.
    emit(SmartFixResult.Success(dto.toDomain()))
}.catch { throwable ->
    emit(SmartFixResult.fromThrowable(throwable))  // maps to SmartFixException subtype
}
```

**Never catch in:**
- Use cases (pass through — they don't add catch blocks)
- ViewModels (they receive only typed `SmartFixResult`)
- Composables (they render only `UiState`)

### Domain layer — pass-through, optionally enrich

Use cases do not catch. They may transform the error to add domain context:

```kotlin
// If the use case knows WHY a not-found means something specific
getUser(id).map { result ->
    if (result is SmartFixResult.Failure &&
        result.exception is NoNetworkException) {
        // Could re-map if you want more specific context — usually not needed
        result
    } else result
}
```

### Presentation layer — translate to UiState strings + actions

ViewModels translate `SmartFixException` to user-facing strings and `RecoveryAction` enum.
Never expose the raw exception to Compose.

```kotlin
is SmartFixResult.Failure -> _uiState.update { state ->
    state.copy(
        isLoading      = false,
        errorMessage   = result.exception.message,     // already user-friendly in SmartFixException
        recoveryAction = result.exception.toRecoveryAction(),
    )
}
```

---

## Exception → Recovery action mapping

```kotlin
fun SmartFixException.toRecoveryAction(): RecoveryAction = when (this) {
    is NoNetworkException        -> RecoveryAction.OpenSettings
    is ServerException           -> RecoveryAction.Retry
    is SessionExpiredException   -> RecoveryAction.NavigateToLogin
    is AuthenticationException   -> RecoveryAction.None   // user must fix their input
    is InvalidRequestException   -> RecoveryAction.None
    is LocalDataException        -> RecoveryAction.Retry
    is UnknownException          -> RecoveryAction.Retry
    else                         -> RecoveryAction.None
}
```

---

## Retry patterns

### Manual retry (ViewModel)
```kotlin
fun onRetry() {
    // Simply re-launch the load — Flow will emit Loading again
    load()
}
```

### Automatic retry with backoff (for transient failures)
```kotlin
flow { emit(fetch()) }
    .retryWhen { cause, attempt ->
        val isTransient = cause is IOException || cause is ServerException
        if (isTransient && attempt < 3) {
            delay(2.0.pow(attempt.toInt()).toLong() * 500L)
            true
        } else false
    }
    .catch { emit(SmartFixResult.fromThrowable(it)) }
```

### When to auto-retry vs surface immediately

| Exception | Action |
|---|---|
| `NoNetworkException` | Surface immediately — retrying without network wastes battery |
| `ServerException(5xx)` | Auto-retry once after 1s, then surface |
| `ServerException(4xx)` | Never retry — it's a client error |
| `AuthenticationException` | Surface immediately — user must fix input |
| `SessionExpiredException` | Trigger re-auth flow, NOT a simple retry |
| `UnknownException` | Auto-retry up to 2×, then surface |
| `LocalDataException` | Retry once, then surface |

---

## Error UI decision tree

```
Is existing content visible on screen?
  YES → Non-blocking snackbar or top banner. Keep content visible.
  NO  →
      Is this the initial load?
        YES → Full-screen error with message + recovery button
        NO  → Inline error in the empty area

Is this a form validation error?
  YES → Inline error below the specific field — never a snackbar
        (use InvalidRequestException.field to target the right field)

Is the error recoverable?
  YES → Show a clear action button (Retry / Log in / Open settings)
  NO  → Show error message + Dismiss only
```

See `references/error-ui-components.md` for ready-to-use Compose components.

---

## Anti-patterns — always refactor when found

```kotlin
// ❌ Throwing from repository (leaks to domain/VM)
fun getTechnician(id: String): Technician = apiService.getTechnician(id)

// ❌ Nullable return to signal failure
fun getTechnician(id: String): Technician? = try { ... } catch (e: Exception) { null }

// ❌ Catching in ViewModel
viewModelScope.launch {
    try { repo.getTechnician(id) } catch (e: Exception) { _error.value = e.message }
}

// ❌ Swallowing CancellationException
catch (e: Exception) { /* also catches coroutine cancellation — very bad */ }
// Fix: use fromThrowable() which re-throws CancellationException

// ❌ UnknownException without cause (your current version)
class UnknownException : SmartFixException("Unknown Exception")
// Fix: class UnknownException(cause: Throwable) : SmartFixException("Unknown Exception", cause)

// ❌ Generic message regardless of exception type
is SmartFixResult.Failure -> state.copy(errorMessage = "Something went wrong")

// ❌ Showing exception class name or stack trace to user
errorMessage = e.javaClass.simpleName
```

---

## Code review checklist

- [ ] No exceptions thrown/leaked above the data layer
- [ ] `CancellationException` is never swallowed (verify `fromThrowable` is used)
- [ ] Every `SmartFixResult.Failure` case is handled in ViewModel (not just `Success`)
- [ ] `UnknownException` carries the original `cause: Throwable`
- [ ] Form errors (`InvalidRequestException`) show inline on the field, not as toast
- [ ] `SessionExpiredException` triggers re-auth flow, not a generic error message
- [ ] `Loading` with `cached` data is used on refresh-over-existing-content scenarios
- [ ] User-facing messages are non-technical (already true if using `SmartFixException.message`)
- [ ] Recovery actions are present for all retryable errors