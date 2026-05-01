# Error Presentation Reference

## ErrorState — the ViewModel → Compose contract

```kotlin
// core/ui/ErrorState.kt
data class ErrorState(
    val message: String,
    val recoveryAction: RecoveryAction = RecoveryAction.Dismiss,
    val recoveryLabel: String = when (recoveryAction) {
        is RecoveryAction.Retry              -> "Try again"
        is RecoveryAction.CheckConnection    -> "Open settings"
        is RecoveryAction.ReAuthenticate     -> "Log in"
        is RecoveryAction.OpenDeviceSettings -> "Open settings"
        is RecoveryAction.Dismiss            -> "Dismiss"
        is RecoveryAction.Custom             -> recoveryAction.label
    },
)
```

---

## Composable error components

### Full-screen error (initial load failure)
```kotlin
@Composable
fun FullScreenError(
    error: ErrorState,
    onAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text  = error.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        if (error.recoveryAction != RecoveryAction.Dismiss) {
            Button(onClick = { onAction(error.recoveryAction) }) {
                Text(error.recoveryLabel)
            }
        }
    }
}
```

### Inline refresh error (content already visible)
```kotlin
// Show as a Snackbar via SnackbarHostState in the parent Scaffold
LaunchedEffect(errorState) {
    errorState?.let {
        val result = snackbarHostState.showSnackbar(
            message     = it.message,
            actionLabel = if (it.recoveryAction != RecoveryAction.Dismiss) it.recoveryLabel else null,
            duration    = SnackbarDuration.Long,
        )
        if (result == SnackbarResult.ActionPerformed) {
            onRecoveryAction(it.recoveryAction)
        }
    }
}
```

### Inline field validation error
```kotlin
OutlinedTextField(
    value         = email,
    onValueChange = onEmailChange,
    isError       = emailError != null,
    supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
    label         = { Text("Email") },
)
```

---

## RecoveryAction handler in ViewModel

```kotlin
fun onRecoveryAction(action: RecoveryAction) {
    when (action) {
        RecoveryAction.Retry             -> loadData()
        RecoveryAction.ReAuthenticate    -> _navEvents.tryEmit(NavEvent.GoToLogin)
        RecoveryAction.CheckConnection,
        RecoveryAction.OpenDeviceSettings -> _navEvents.tryEmit(NavEvent.OpenSystemSettings)
        RecoveryAction.Dismiss           -> _uiState.update { it.copy(error = null) }
        is RecoveryAction.Custom         -> action.action()
    }
}
```