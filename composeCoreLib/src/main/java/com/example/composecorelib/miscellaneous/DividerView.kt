package com.example.composecorelib.miscellaneous

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DividerView() {
    Divider(
        Modifier
            .fillMaxWidth()
            .width(1.dp)
            .padding(10.dp, 10.dp),
        color = Color.LightGray)
}

@Composable
@Preview(showBackground = true)
fun PreviewDividerView() {
    DividerView()
}
