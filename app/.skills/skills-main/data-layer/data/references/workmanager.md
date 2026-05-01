# WorkManager Reference — Background Sync, Upload, Retry

## Table of Contents
1. Worker types
2. CoroutineWorker (standard)
3. Constraints and scheduling
4. Chaining workers
5. Progress reporting
6. Koin injection in Workers
7. Triggering from Repository
8. Koin wiring

---

## 1. Worker Types

| Type | When to use |
|---|---|
| `CoroutineWorker` | Network calls, DB writes — 99% of cases |
| `ListenableWorker` | Interop with callback-based APIs |
| `Worker` | Synchronous, CPU-only tasks (rare) |

Always use `CoroutineWorker`. It runs on `Dispatchers.Default` by default; override with
`withContext(Dispatchers.IO)` for I/O work.

---

## 2. CoroutineWorker

```kotlin
// data/worker/SyncUsersWorker.kt
class SyncUsersWorker(
    context: Context,
    params: WorkerParameters,
    private val userRepository: UserRepository,   // injected via Koin
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            userRepository.syncUsers()
            Result.success()
        } catch (e: NoNetworkException) {
            // Retry — network is recoverable
            if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
        } catch (e: SmartFixException) {
            // Non-recoverable domain error — fail with output data for debugging
            Result.failure(
                workDataOf("error" to e.message)
            )
        } catch (e: Exception) {
            if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "sync_users_work"
        private const val MAX_RETRIES = 3

        fun buildRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncUsersWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()

        fun buildOneTimeRequest(userId: String): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SyncUsersWorker>()
                .setInputData(workDataOf("user_id" to userId))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
    }
}
```

---

## 3. Constraints and Scheduling

```kotlin
// Enqueue periodic sync (idempotent — use KEEP to not restart if already running)
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    SyncUsersWorker.WORK_NAME,
    ExistingPeriodicWorkPolicy.KEEP,
    SyncUsersWorker.buildRequest(),
)

// Enqueue one-time (e.g., triggered by user action)
WorkManager.getInstance(context).enqueueUniqueWork(
    "upload_report_${reportId}",
    ExistingWorkPolicy.REPLACE,
    UploadReportWorker.buildRequest(reportId),
)
```

---

## 4. Chaining Workers

```kotlin
WorkManager.getInstance(context)
    .beginUniqueWork("full_sync", ExistingWorkPolicy.REPLACE,
        OneTimeWorkRequestBuilder<SyncUsersWorker>().build())
    .then(OneTimeWorkRequestBuilder<SyncOrdersWorker>().build())
    .then(OneTimeWorkRequestBuilder<NotifyUserWorker>().build())
    .enqueue()
```

---

## 5. Progress Reporting

```kotlin
// In CoroutineWorker
override suspend fun doWork(): Result {
    setProgress(workDataOf("progress" to 0))
    // ... do work ...
    setProgress(workDataOf("progress" to 50))
    // ... more work ...
    setProgress(workDataOf("progress" to 100))
    return Result.success()
}

// Observer in ViewModel
WorkManager.getInstance(context)
    .getWorkInfoByIdLiveData(request.id)
    .observe(viewLifecycleOwner) { info ->
        val progress = info?.progress?.getInt("progress", 0) ?: 0
        updateProgressBar(progress)
    }
```

---

## 6. Koin Injection in Workers

Use `WorkerFactory` with Koin:

```kotlin
// di/KoinWorkerFactory.kt
class KoinWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncUsersWorker::class.qualifiedName ->
                SyncUsersWorker(appContext, workerParameters, KoinJavaComponent.get(UserRepository::class.java))
            else -> null  // fall through to default factory
        }
    }
}

// Application.kt
class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@App); modules(appModules) }

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(KoinWorkerFactory())
                .build()
        )
    }
}

// AndroidManifest.xml — disable default initializer
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    tools:node="remove" />
```

---

## 7. Triggering from Repository

For offline-first: queue a sync WorkManager task whenever a local write happens.

```kotlin
// data/repository/OrderRepositoryImpl.kt
override suspend fun createOrder(order: Order) {
    val entity = order.toEntity().copy(isSynced = false)
    orderDao.insert(entity)
    // Schedule upload in background
    WorkManager.getInstance(context).enqueue(
        UploadOrderWorker.buildRequest(order.id)
    )
}
```

---

## 8. Koin Wiring

```kotlin
val workerModule = module {
    single { KoinWorkerFactory() }
    // Workers themselves are NOT in Koin — KoinWorkerFactory constructs them manually
    // Dependencies injected into workers should already be in Koin (repositories, DAOs)
}
```