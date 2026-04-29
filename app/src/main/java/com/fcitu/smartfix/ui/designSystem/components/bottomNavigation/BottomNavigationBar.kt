package com.fcitu.smartfix.ui.designSystem.components.bottomNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fcitu.smartfix.R

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItemIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {},
    content: @Composable BottomNavigationScope.() -> Unit = {},
) {
    val scope = remember { BottomNavigationScopeImpl() }.apply {
        items.clear()
        content()
    }

    BottomNavigationBarContent(
        items = scope.items,
        selectedItemIndex = selectedItemIndex,
        onItemClick = { item ->
            val index = scope.items.indexOf(item)
            onItemSelected(index)
            scope.items[index].entry.invoke()
        },
        modifier = modifier.background(Color(0xFFFFFFFF))
    )
}

@Preview
@Composable
private fun PreviewBottomNavigationBar() {
    Box(Modifier.fillMaxSize()) {
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_home_selected),
                notSelectedIcon = painterResource(R.drawable.ic_home),
                title = stringResource(R.string.home),
                entry = { }
            )

            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_chat_selected),
                notSelectedIcon = painterResource(R.drawable.ic_chat),
                title = stringResource(R.string.chat),
                entry = { }
            )
        }
    }
}