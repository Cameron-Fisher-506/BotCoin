package com.example.composecorelib.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonView(title: String, shape: Shape = RectangleShape, onClick: () -> Unit) {
    Surface(Modifier.wrapContentSize()) {
        Button(
            { onClick() },
            Modifier
                .padding(20.dp, 10.dp)
                .fillMaxWidth(1f),
            shape = shape,
        ) {
            Text(title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonView() {
    ButtonView("Button") {}
}