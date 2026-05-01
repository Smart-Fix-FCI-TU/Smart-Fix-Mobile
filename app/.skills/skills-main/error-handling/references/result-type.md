# SmartFixResult — Complete Implementation

## Core type

```kotlin
// domain/result/SmartFixResult.kt
package com.fcitu.smartfix.domain.result

import com.fcitu.smartfix.domain.exception.SmartFixException
import com.fcitu.smartfix.domain.exception.toSmartFixException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed class SmartFixResult<out T> {

    data class Success<out T>(val data: T) : SmartFixResult<T>()

    data class Loading<out T>(val cached: T? = null) : SmartFixResult<T>()

    data class Failure<out T>(val exception: SmartFixException) : SmartFixResult<T>()

    // ─── Convenience properties ───────────────────────────────────────────────

    val isSuccess get() = this is Success
    val isLoading get() = this is Loading
    val isFailure get() = this is Failure

    fun getOrNull(): T? = (this as? Success)?.data

    // ─── Transform ────────────────────────────────────────────────────────────

    fun <R> map(transform: (T) -> R): SmartFixResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Loading -> Loading(cached?.let(transform))
        is Failure -> Failure(exception)
    }

    // ─── Factory ──────────────────────────────────────────────────────────────

    companion object {
        fun <T> fromThrowable(throwable: Throwable): SmartFixResult<T> =
            Failure(throwable.toSmartFixException())
    }
}

// ─── Flow extensions ──────────────────────────────────────────────────────────

/**
 * Catches any throwable from the upstream flow and emits it as SmartFixResult.Failure.
 * Use this at the end of every repository Flow chain.
 */
fun <T> Flow<SmartFixResult<T>>.catchAsFailure(): Flow<SmartFixResult<T>> =
    catch { throwable -> emit(SmartFixResult.fromThrowable(throwable)) }

fun <T> Flow<SmartFixResult<T>>.onSuccess(
    action: suspend (T) -> Unit,
): Flow<SmartFixResult<T>> = onEach { if (it is SmartFixResult.Success) action(it.data) }

fun <T> Flow<SmartFixResult<T>>.onFailure(
    action: suspend (SmartFixException) -> Unit,
): Flow<SmartFixResult<T>> = onEach { if (it is SmartFixResult.Failure) action(it.exception) }

fun <T, R> Flow<SmartFixResult<T>>.mapResult(
    transform: (T) -> R,
): Flow<SmartFixResult<R>> = map { it.map(transform) }
```

---

## Usage examples

### Repository — wrapping a single suspend call
```kotlin
override fun getUserProfile(id: String): Flow<SmartFixResult<UserProfile>> = flow {
    emit(SmartFixResult.Loading())
    val dto = apiService.getUser(id)
    emit(SmartFixResult.Success(dto.toDomain()))
}.catchAsFailure()
```

### Repository — offline-first with networkBoundResource
```kotlin
override fun getTechnicians(): Flow<SmartFixResult<List<Technician>>> =
    smartFixNetworkBoundResource(
        query     = { dao.observeAll().map { it.map { e -> e.toDomain() } } },
        fetch     = { apiService.getTechnicians() },
        saveFetch = { dtos -> dao.upsertAll(dtos.map { it.toDomain().toEntity() }) },
        toDomain  = { entities -> entities.map { it.toDomain() } },
    )
```

### ViewModel — collecting and mapping to UiState
```kotlin
private fun load() {
    getAvailableTechnicians()
        .onEach { result ->
            _uiState.update { state ->
                when (result) {
                    is SmartFixResult.Loading -> state.copy(
                        isLoading   = true,
                        technicians = result.cached ?: state.technicians,
                    )
                    is SmartFixResult.Success -> state.copy(
                        isLoading   = false,
                        technicians = result.data,
                        error       = null,
                    )
                    is SmartFixResult.Failure -> state.copy(
                        isLoading      = false,
                        errorMessage   = result.exception.message,
                        recoveryAction = result.exception.toRecoveryAction(),
                    )
                }
            }
        }
        .launchIn(viewModelScope)
}
```

### Use case — chaining two operations
```kotlin
class BookTechnicianUseCase(
    private val technicianRepository: TechnicianRepository,
    private val bookingRepository: BookingRepository,
) {
    operator fun invoke(technicianId: String, slot: TimeSlot): Flow<SmartFixResult<Booking>> =
        flow {
            emit(SmartFixResult.Loading())
            // Validate technician is available first
            val techResult = technicianRepository.getTechnician(technicianId).first()
            if (techResult is SmartFixResult.Failure) {
                emit(techResult)
                return@flow
            }
            // Then create the booking
            emitAll(bookingRepository.createBooking(technicianId, slot))
        }.catchAsFailure()
}
```