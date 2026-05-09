---
name: android-localization
description: >
  Use this skill whenever an Android developer needs to localize or display string resources
  from a ViewModel, UseCase, or any non-UI class without injecting Context into the wrong layer.
  Trigger on phrases like: "hardcoded string in ViewModel", "error message localization",
  "UiText", "StringResourceProvider", "Context in ViewModel", "how do I show a translated
  string from ViewModel", "localize error message", "string resource in ViewModel",
  "Context leak", "AndroidViewModel alternative", or any question about displaying
  localized strings in MVVM / Clean Architecture on Android.
  Covers two patterns: UiText sealed class and StringResourceProvider interface.
  Always use this skill before writing any Android localization-related code.
---

# Android ViewModel Localization Skill

## Problem

ViewModels should **never** hold a `Context` reference — it causes memory leaks and
violates separation of concerns. But sometimes you need to produce a human-readable,
localized string (e.g., error messages) from inside a ViewModel or UseCase.

This skill covers the two canonical solutions and when to pick each.

---

## Pattern Decision Guide

| Situation | Recommended Pattern |
|---|---|
| ViewModel produces error/status messages for UI state | **UiText** (sealed class) |
| Multiple non-UI classes (UseCases, Repos) need string access | **StringResourceProvider** (interface + DI) |
| You want zero Context anywhere below the UI layer | **UiText** |
| You prefer resolving strings eagerly (e.g., logging) | **StringResourceProvider** |
| Project uses Koin for DI | Either — both are Koin-friendly |

---

## Pattern 1 — UiText Sealed Class

**Concept:** The ViewModel emits *intent* (a resource ID or a raw string), and the UI
(Activity/Fragment/Composable) is the only place that resolves it into a real string.
No Context ever enters the ViewModel.

### Step 1 — Define UiText

```kotlin
// core/ui/UiText.kt
sealed class UiText {

    /** A plain hardcoded string (useful for dynamic/server-driven content) */
    data class DynamicString(val value: String) : UiText()

    /** A string resource ID, with optional format args */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    /** Resolve to an actual String — call only from the UI layer */
    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
```

### Step 2 — Use in ViewModel

```kotlin
// feature/login/LoginViewModel.kt
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            _uiState.update { state ->
                state.copy(
                    errorMessage = when (result) {
                        is Result.Success -> null
                        is Result.Error.NetworkError ->
                            UiText.StringResource(R.string.error_network)
                        is Result.Error.InvalidCredentials ->
                            UiText.StringResource(R.string.error_invalid_credentials)
                        is Result.Error.Unknown ->
                            // Mix dynamic + resource if needed:
                            UiText.StringResource(
                                R.string.error_unknown_with_code,
                                result.code   // format arg
                            )
                    }
                )
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
```

### Step 3 — Resolve in the UI

**Composable:**
```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    uiState.errorMessage?.let { uiText ->
        Text(text = uiText.asString(context))
    }
}
```

**Fragment:**
```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    viewModel.uiState.collectLatest { state ->
        state.errorMessage?.let {
            Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT).show()
        }
    }
}
```

---

## Pattern 2 — StringResourceProvider (Interface + Koin)

**Concept:** Define an interface that abstracts `getString`. Inject it anywhere below the
UI layer (ViewModels, UseCases, etc.) via Koin. The implementation holds an
`applicationContext` — which is safe (it's not an Activity context).

### Step 1 — Define the interface

```kotlin
// core/resources/StringResourceProvider.kt
interface StringResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg args: Any): String
}
```

### Step 2 — Implement with applicationContext

```kotlin
// core/resources/AndroidStringResourceProvider.kt
class AndroidStringResourceProvider(
    private val context: Context  // always pass applicationContext, never Activity
) : StringResourceProvider {

    override fun getString(@StringRes resId: Int): String =
        context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg args: Any): String =
        context.getString(resId, *args)
}
```

> ⚠️ **Always pass `applicationContext`**, never an Activity or Fragment context.  
> Application context lives as long as the app process — no leak risk.

### Step 3 — Register in a Koin Module

```kotlin
// di/resourceModule.kt
val resourceModule = module {
    single<StringResourceProvider> {
        AndroidStringResourceProvider(context = androidApplication())
        // androidApplication() is Koin's way to get applicationContext safely
    }
}
```

Register it in your `Application` class:

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(resourceModule, /* other modules */)
        }
    }
}
```

### Step 4 — Inject and use in ViewModel

```kotlin
// feature/profile/ProfileViewModel.kt
class ProfileViewModel(
    private val stringProvider: StringResourceProvider,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUser(id: String) {
        viewModelScope.launch {
            val result = getUserUseCase(id)
            _uiState.update {
                it.copy(
                    errorMessage = when (result) {
                        is Result.Success -> null
                        is Result.Error ->
                            stringProvider.getString(R.string.error_loading_profile)
                    }
                )
            }
        }
    }
}

data class ProfileUiState(
    val errorMessage: String? = null
)
```

Register the ViewModel in your Koin module:

```kotlin
val featureModule = module {
    viewModel { ProfileViewModel(get(), get()) }
}
```

### Step 4b — Inject into a UseCase

```kotlin
// domain/usecase/ValidateEmailUseCase.kt
class ValidateEmailUseCase(
    private val strings: StringResourceProvider
) {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank())
            return ValidationResult.Error(strings.getString(R.string.error_email_blank))
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return ValidationResult.Error(strings.getString(R.string.error_email_invalid))
        return ValidationResult.Success
    }
}
```

Register in Koin:

```kotlin
val domainModule = module {
    factory { ValidateEmailUseCase(get()) }
}
```

---

## Combining Both Patterns

You can use **UiText** for UI state and **StringResourceProvider** for non-UI classes
(logging, analytics labels, validation) in the same project — they are complementary.

```
UI Layer         →  resolves UiText via context
ViewModel        →  emits UiText in UiState
UseCase / Repo   →  may use StringResourceProvider for non-UI strings
                    (e.g., error logging, analytics event names)
```

---

## Common Mistakes to Avoid

| ❌ Don't | ✅ Do instead |
|---|---|
| `class MyViewModel(val context: Context)` | Use UiText or StringResourceProvider |
| Extend `AndroidViewModel` just for strings | Use one of the two patterns above |
| Inject `Activity` context into ViewModel module | Use `androidApplication()` in Koin module |
| Hardcode English strings in ViewModel | Emit `UiText.StringResource(R.string.xxx)` |
| Store `Activity` reference in UseCase | Pass StringResourceProvider instead |

---

## String Resource File Reminder

For every `R.string.xxx` referenced in ViewModel/UseCase code, make sure it exists:

```xml
<!-- res/values/strings.xml -->
<resources>
    <string name="error_network">No internet connection. Please try again.</string>
    <string name="error_invalid_credentials">Email or password is incorrect.</string>
    <string name="error_unknown_with_code">Something went wrong (code: %1$d).</string>
    <string name="error_loading_profile">Failed to load profile. Please retry.</string>
    <string name="error_email_blank">Email cannot be empty.</string>
    <string name="error_email_invalid">Please enter a valid email address.</string>
</resources>
```

And add translations to `res/values-ar/strings.xml`, `res/values-fr/strings.xml`, etc.

---

## See Also

- `references/uitext-extensions.md` — Extra UiText helpers (Snackbar, Dialog, etc.)
- `references/testing.md` — How to unit-test ViewModels with both patterns