package com.fcitu.smartfix.ui.theme.designSystem.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.button.TextButton
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text

@Composable
fun Section(
    title: String,
    actionName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            modifier = Modifier.weight(1f)
        )

        TextButton(
            text = actionName,
            trailingIcon = painterResource(R.drawable.ic_arrow_right),
            onClick = onClick
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F4F7)
@Composable
private fun PreviewSection() {
    Section(
        modifier = Modifier.fillMaxWidth(),
        title = "Section name",
        actionName = "see all",
        onClick = {}
    )
}