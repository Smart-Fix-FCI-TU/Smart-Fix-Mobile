# UiText Extensions & Helpers

## Snackbar Support

```kotlin
fun UiText.showAsSnackbar(view: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, asString(view.context), duration).show()
}
```

## AlertDialog Support

```kotlin
fun UiText.asAlertDialog(context: Context, title: UiText? = null): AlertDialog =
    AlertDialog.Builder(context)
        .setTitle(title?.asString(context))
        .setMessage(asString(context))
        .setPositiveButton(android.R.string.ok, null)
        .create()
```

## Composable Extension

Avoids needing to pass `LocalContext` manually everywhere:

```kotlin
@Composable
fun UiText.asAnnotatedString(): AnnotatedString {
    val context = LocalContext.current
    return AnnotatedString(asString(context))
}

// Usage in Composable:
Text(text = uiState.errorMessage?.asAnnotatedString() ?: AnnotatedString(""))
```

## UiText with Plurals

```kotlin
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UiText()
    class PluralResource(
        @PluralsRes val resId: Int,
        val count: Int,
        vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
        is PluralResource -> context.resources.getQuantityString(resId, count, *args)
    }
}
```

Example strings.xml:
```xml
<plurals name="items_selected">
    <item quantity="one">%1$d item selected</item>
    <item quantity="other">%1$d items selected</item>
</plurals>
```

Usage:
```kotlin
UiText.PluralResource(R.plurals.items_selected, count = 3, 3)
```