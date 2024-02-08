package com.example.composecorelib.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomInputViewCompose(
    title: String = "Title",
    value: String = "Value",
    description: String = "",
    errorMessage: String = "",
    onValueChange: (String) -> Unit
) {
    Surface(modifier = Modifier.wrapContentSize()) {
        Column(modifier = Modifier.fillMaxWidth(1f),) {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.titleSmall
            )
            if (errorMessage.isNotEmpty()) {
                TextFieldView(value) { onValueChange(it) }
            } else {
                TextFieldView(value, true) { onValueChange(it) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun TextFieldView(value: String, isError: Boolean = false, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        isError = isError,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth(1f),
        onValueChange = { onValueChange(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CustomInputViewCompose {}
}
