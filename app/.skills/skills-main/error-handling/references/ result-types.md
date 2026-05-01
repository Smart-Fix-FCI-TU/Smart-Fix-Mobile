# Result Types — Complete Implementation

## AppResult

```kotlin
// core/result/AppResult.kt
package com.app.core.result

sealed class AppResult<out T> {

    data class Success<out T>(val data: T) : AppResult<T>()

    data class Loading<out T>(val cached: T? = null) : AppResult<T>()

    data class Error<out T>(val error: AppError) : AppResult<T>()

    val isSuccess get() = this is Success
    val isLoading get() = this is Loading
    val isError   get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data

    fun <R> map(transform: (T) -> R): AppResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Loading -> Loading(cached?.let(transform))
        is Error   -> Error(error)
    }

    companion object {
        fun <T> fromThrowable(throwable: Throwable): AppResult<T> =
            Error(AppError.fromThrowable(throwable))

        suspend fun <T> of(block: suspend () -> T): AppResult<T> = try {
            Success(block())
        } catch (e: Throwable) {
            fromThrowable(e)
        }
    }
}

// Extension for Flow
fun <T> Flow<AppResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<AppResult<T>> =
    onEach { if (it is AppResult.Success) action(it.data) }

fun <T> Flow<AppResult<T>>.onError(action: suspend (AppError) -> Unit): Flow<AppResult<T>> =
    onEach { if (it is AppResult.Error) action(it.error) }
```

---

## AppError

```kotlin
// core/result/AppError.kt
package com.app.core.result

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

sealed class AppError {

    abstract val userMessage: String
    abstract val recoveryAction: RecoveryAction

    // ─── Network errors ────────────────────────────────────────────────────────

    sealed class Network : AppError() {

        object NoConnection : Network() {
            override val userMessage = "No internet connection. Please check your network."
            override val recoveryAction = RecoveryAction.CheckConnection
        }

        object Timeout : Network() {
            override val userMessage = "The request timed out. Please try again."
            override val recoveryAction = RecoveryAction.Retry
        }

        data class ServerError(val code: Int, val serverMessage: String? = null) : Network() {
            override val userMessage = when (code) {
                in 500..599 -> "Our servers are having trouble. Please try again later."
                else        -> "Unexpected server response ($code)."
            }
            override val recoveryAction = when (code) {
                in 500..599 -> RecoveryAction.Retry
                else        -> RecoveryAction.Dismiss
            }
        }

        object Unauthorized : Network() {
            override val userMessage = "Your session has expired. Please log in again."
            override val recoveryAction = RecoveryAction.ReAuthenticate
        }

        object Forbidden : Network() {
            override val userMessage = "You don't have permission to do that."
            override val recoveryAction = RecoveryAction.Dismiss
        }

        data class Unknown(val cause: Throwable) : Network() {
            override val userMessage = "A network error occurred. Please try again."
            override val recoveryAction = RecoveryAction.Retry
        }
    }

    // ─── Local / persistence errors ────────────────────────────────────────────

    sealed class Local : AppError() {

        data class NotFound(val query: String) : Local() {
            override val userMessage = "Nothing found."
            override val recoveryAction = RecoveryAction.Dismiss
        }

        data class DatabaseError(val cause: Throwable) : Local() {
            override val userMessage = "A local data-layer error occurred."
            override val recoveryAction = RecoveryAction.Retry
        }

        object DiskFull : Local() {
            override val userMessage = "Your device storage is full."
            override val recoveryAction = RecoveryAction.OpenDeviceSettings
        }
    }

    // ─── Domain / business errors ──────────────────────────────────────────────

    sealed class Domain : AppError() {

        data class ValidationError(val field: String, val reason: String) : Domain() {
            override val userMessage = reason
            override val recoveryAction = RecoveryAction.Dismiss
        }

        data class BusinessRuleViolation(val rule: String) : Domain() {
            override val userMessage = "This action isn't allowed right now."
            override val recoveryAction = RecoveryAction.Dismiss
        }

        data class Conflict(val resource: String) : Domain() {
            override val userMessage = "$resource already exists."
            override val recoveryAction = RecoveryAction.Dismiss
        }
    }

    // ─── Catch-all ─────────────────────────────────────────────────────────────

    data class Unknown(val cause: Throwable) : AppError() {
        override val userMessage = "An unexpected error occurred."
        override val recoveryAction = RecoveryAction.Retry
    }

    // ─── Factory ───────────────────────────────────────────────────────────────

    companion object {
        fun fromThrowable(throwable: Throwable): AppError = when (throwable) {
            is kotlinx.coroutines.CancellationException -> throw throwable  // never swallow
            is UnknownHostException   -> Network.NoConnection
            is SocketTimeoutException -> Network.Timeout
            is IOException            -> Network.Unknown(throwable)
            is HttpException          -> when (throwable.code()) {
                401  -> Network.Unauthorized
                403  -> Network.Forbidden
                else -> Network.ServerError(throwable.code(), throwable.message())
            }
            is android.database.SQLException -> Local.DatabaseError(throwable)
            else -> Unknown(throwable)
        }
    }
}

// ─── Recovery actions ──────────────────────────────────────────────────────────

sealed interface RecoveryAction {
    object Retry               : RecoveryAction
    object Dismiss             : RecoveryAction
    object CheckConnection     : RecoveryAction
    object ReAuthenticate      : RecoveryAction
    object OpenDeviceSettings  : RecoveryAction
    data class Custom(val label: String, val action: () -> Unit) : RecoveryAction
}
```