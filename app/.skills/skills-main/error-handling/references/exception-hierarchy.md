# Exception Hierarchy — SmartFix Project

Complete hierarchy extending your existing exceptions. Existing code is marked,
new additions are marked [NEW] with rationale.

---

## Full hierarchy

```kotlin
// domain/exception/SmartFixException.kt
package com.fcitu.smartfix.domain.exception

// ─── Base ─────────────────────────────────────────────────────────────────────

open class SmartFixException(
    message: String,
    cause: Throwable? = null,       // [EXTENDED] was: no cause param
) : Exception(message, cause)

// ─── Network / connectivity ───────────────────────────────────────────────────

class NoNetworkException : SmartFixException("No internet connection. Please check your network.")

// [NEW] — was missing. 5xx responses need a typed home.
data class ServerException(
    val code: Int,
    val serverMessage: String? = null,
) : SmartFixException(
    message = when (code) {
        in 500..599 -> "Our servers are having trouble. Please try again later."
        else        -> "Unexpected server response ($code)."
    }
)

// ─── Request errors ───────────────────────────────────────────────────────────

// [EXTENDED] — added `field` so the UI can show inline errors on the right input
class InvalidRequestException(
    val field: String? = null,          // null = general request error
    reason: String = "Invalid request",
) : SmartFixException(reason)

// ─── Authentication ───────────────────────────────────────────────────────────

open class AuthenticationException(message: String) : SmartFixException(message)

class UserNotRegisteredException : AuthenticationException("Phone number not registered")

class InvalidCountryCodeException(countryCode: String) :
    AuthenticationException("Country code '$countryCode' is not valid or not supported yet")

class InvalidMobileNumberException(mobileNumber: String) :
    AuthenticationException("Mobile number '$mobileNumber' doesn't match validation")

class InvalidPasswordException : AuthenticationException("Password doesn't match validations")

// [NEW] — for when refresh token is expired / revoked. Distinct from InvalidPasswordException.
class SessionExpiredException : AuthenticationException("Your session has expired. Please log in again.")

// ─── Local persistence ────────────────────────────────────────────────────────

// [NEW] — Room/DataStore failures need a typed home instead of falling into UnknownException
data class LocalDataException(
    override val cause: Throwable,
) : SmartFixException("A local data-layer error occurred.", cause)

// ─── Catch-all ────────────────────────────────────────────────────────────────

// [EXTENDED] — added cause so you can inspect the original error in crash reporting
class UnknownException(
    cause: Throwable? = null,
) : SmartFixException("An unexpected error occurred.", cause)
```

---

## fromThrowable factory

Maps any raw `Throwable` to the closest `SmartFixException` subtype.
Place this as a companion object function on `SmartFixException`, or as a top-level
function in `domain/exception/ExceptionMapper.kt`.

```kotlin
// domain/exception/ExceptionMapper.kt
package com.fcitu.smartfix.domain.exception

import android.database.SQLException
import io.ktor.client.plugins.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toSmartFixException(): SmartFixException {
    // CRITICAL: never swallow coroutine cancellation
    if (this is kotlinx.coroutines.CancellationException) throw this

    // Already one of ours — return as-is
    if (this is SmartFixException) return this

    return when (this) {
        // ── Connectivity ──────────────────────────────────────────────────────
        is UnknownHostException   -> NoNetworkException()
        is SocketTimeoutException -> NoNetworkException()
        is IOException            -> NoNetworkException()

        // ── Retrofit HTTP errors ──────────────────────────────────────────────
        is HttpException -> when (code()) {
            401  -> SessionExpiredException()
            403  -> AuthenticationException("You don't have permission to perform this action.")
            422  -> InvalidRequestException(reason = message() ?: "Invalid request")
            in 500..599 -> ServerException(code = code(), serverMessage = message())
            else -> ServerException(code = code(), serverMessage = message())
        }

        // ── Ktor HTTP errors ──────────────────────────────────────────────────
        is ResponseException -> when (response.status.value) {
            401  -> SessionExpiredException()
            403  -> AuthenticationException("You don't have permission to perform this action.")
            422  -> InvalidRequestException(reason = message ?: "Invalid request")
            in 500..599 -> ServerException(
                code          = response.status.value,
                serverMessage = message,
            )
            else -> ServerException(code = response.status.value, serverMessage = message)
        }

        // ── Local persistence ─────────────────────────────────────────────────
        is SQLException -> LocalDataException(cause = this)

        // ── Catch-all ─────────────────────────────────────────────────────────
        else -> UnknownException(cause = this)
    }
}
```

---

## toRecoveryAction extension

```kotlin
// domain/exception/RecoveryAction.kt
package com.fcitu.smartfix.domain.exception

enum class RecoveryAction { None, Retry, NavigateToLogin, OpenSettings }

fun SmartFixException.toRecoveryAction(): RecoveryAction = when (this) {
    is NoNetworkException        -> RecoveryAction.OpenSettings
    is ServerException           -> RecoveryAction.Retry
    is SessionExpiredException   -> RecoveryAction.NavigateToLogin
    is LocalDataException        -> RecoveryAction.Retry
    is UnknownException          -> RecoveryAction.Retry
    is AuthenticationException   -> RecoveryAction.None   // user must fix input
    is InvalidRequestException   -> RecoveryAction.None
    else                         -> RecoveryAction.None
}
```