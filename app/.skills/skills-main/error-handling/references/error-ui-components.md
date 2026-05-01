# Error UI Components — Compose

Ready-to-use composables for every error scenario in SmartFix.

---

## Full-screen error (initial load failure — no content to show)

```kotlin
// presentation/common/FullScreenError.kt
@Composable
fun FullScreenError(
    message: String,
    recoveryAction: RecoveryAction,
    onAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = Icons.Rounded.ErrorOutline,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.error,
            modifier           = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text      = message,
            style     = MaterialTheme.typography.bodyLarge,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (recoveryAction != RecoveryAction.None) {
            Spacer(Modifier.height(20.dp))
            Button(onClick = { onAction(recoveryAction) }) {
                Text(recoveryAction.label())
            }
        }
    }
}
```

---

## Snackbar error (content is already visible — non-blocking)

```kotlin
// In your Screen composable's parent Scaffold:
val snackbarHostState = remember { SnackbarHostState() }

Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
    // content...
}

// In Route composable — collect error once and show snackbar
LaunchedEffect(uiState.errorMessage) {
    val message = uiState.errorMessage ?: return@LaunchedEffect
    val actionLabel = uiState.recoveryAction
        .takeIf { it != RecoveryAction.None }
        ?.label()

    val result = snackbarHostState.showSnackbar(
        message     = message,
        actionLabel = actionLabel,
        duration    = SnackbarDuration.Long,
    )
    if (result == SnackbarResult.ActionPerformed) {
        onRecoveryAction(uiState.recoveryAction)
    }
}
```

---

## Inline field error (form validation — InvalidRequestException)

```kotlin
// Pass the field name from InvalidRequestException to highlight the right field
@Composable
fun SmartFixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    fieldName: String,                         // matches InvalidRequestException.field
    fieldError: InvalidRequestException?,      // null = no error
    modifier: Modifier = Modifier,
) {
    val hasError = fieldError?.field == fieldName

    OutlinedTextField(
        value          = value,
        onValueChange  = onValueChange,
        label          = { Text(label) },
        isError        = hasError,
        supportingText = if (hasError) {
            { Text(fieldError!!.message ?: "Invalid input",
                   color = MaterialTheme.colorScheme.error) }
        } else null,
        modifier       = modifier.fillMaxWidth(),
    )
}

// Usage:
SmartFixTextField(
    value         = phoneNumber,
    onValueChange = viewModel::onPhoneChanged,
    label         = "Phone number",
    fieldName     = "phone_number",
    fieldError    = uiState.fieldError,   // InvalidRequestException? from UiState
)
```

---

## Inline refresh error banner (content visible, refresh failed)

```kotlin
@Composable
fun ErrorBanner(
    message: String,
    recoveryAction: RecoveryAction,
    onAction: (RecoveryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color    = MaterialTheme.colorScheme.errorContainer,
        shape    = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier            = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text     = message,
                style    = MaterialTheme.typography.bodySmall,
                color    = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f),
            )
            if (recoveryAction != RecoveryAction.None) {
                TextButton(onClick = { onAction(recoveryAction) }) {
                    Text(
                        text  = recoveryAction.label(),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        }
    }
}
```

---

## RecoveryAction label extension

```kotlin
fun RecoveryAction.label(): String = when (this) {
    RecoveryAction.Retry           -> "Try again"
    RecoveryAction.NavigateToLogin -> "Log in again"
    RecoveryAction.OpenSettings    -> "Open settings"
    RecoveryAction.None            -> ""
}
```

---

## UiState error shape (recommended)

Use this in every feature's UiState that can have errors:

```kotlin
data class ErrorUiState(
    val message: String,
    val recoveryAction: RecoveryAction,
    // If this is set, it's a field-level validation error
    val fieldException: InvalidRequestException? = null,
)

// In your feature UiState:
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: ErrorUiState? = null,           // null = no error
    // field-level: check error.fieldException?.field == "phone_number"
)
```