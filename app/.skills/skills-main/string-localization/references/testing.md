# Testing ViewModels with Localization Patterns

## Testing with UiText (no mocking needed)

Since UiText just carries a resource ID, you can assert on the ID directly — no Context needed.

```kotlin
@Test
fun `login with wrong password emits invalid credentials error`() = runTest {
    val viewModel = LoginViewModel(FakeLoginUseCase(Result.Error.InvalidCredentials))

    viewModel.login("user@test.com", "wrong")

    val state = viewModel.uiState.first()
    val error = state.errorMessage
    assertTrue(error is UiText.StringResource)
    assertEquals(R.string.error_invalid_credentials, (error as UiText.StringResource).resId)
}
```

## Testing with StringResourceProvider (use a fake)

Create a simple fake that returns the key as the value — no Android runtime needed:

```kotlin
class FakeStringResourceProvider : StringResourceProvider {
    override fun getString(resId: Int): String = resId.toString()
    override fun getString(resId: Int, vararg args: Any): String =
        "${resId}: ${args.joinToString()}"
}
```

```kotlin
@Test
fun `loadUser failure sets error message`() = runTest {
    val strings = FakeStringResourceProvider()
    val viewModel = ProfileViewModel(
        stringProvider = strings,
        getUserUseCase = FakeGetUserUseCase(Result.Error)
    )

    viewModel.loadUser("123")

    val state = viewModel.uiState.first()
    assertEquals(R.string.error_loading_profile.toString(), state.errorMessage)
}
```

## Tips

- Prefer **UiText** when you want tests that are completely independent of Android resources.
- Prefer **FakeStringResourceProvider** when the exact string content matters in tests (e.g., validation error messages fed to downstream logic).
- Never use `ApplicationContext` in unit tests — that's why both patterns above avoid it.