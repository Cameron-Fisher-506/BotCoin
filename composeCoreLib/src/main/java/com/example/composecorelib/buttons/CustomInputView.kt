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
    value: String = "",
    placeholder: String = "Placeholder",
    description: String = "Description",
    errorMessage: String = "Error Message",
    onValueChange: (String) -> Unit
) {
    Surface(Modifier.wrapContentSize()) {
        Column(Modifier.fillMaxWidth(1f),) {
            Text(
                title,
                Modifier.padding(20.dp,10.dp),
                style = MaterialTheme.typography.titleSmall
            )
            if (errorMessage.isNotEmpty()) {
                TextFieldView(placeholder) { onValueChange(it) }
            } else {
                TextFieldView(placeholder, true) { onValueChange(it) }
            }

            Row(
                Modifier.fillMaxWidth(1f),
                Arrangement.SpaceBetween
            ) {
                if (description.isNotEmpty()) {
                    Text(
                        description,
                        Modifier.padding(20.dp, 10.dp)
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        Modifier.padding(20.dp, 10.dp),
                        Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun TextFieldView(placeholder: String, isError: Boolean = false, onValueChange: (String) -> Unit) {
    TextField(
        value = "",
        isError = isError,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 0.dp)
            .fillMaxWidth(1f),
        placeholder = { Text(placeholder) },
        onValueChange = { onValueChange(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CustomInputViewCompose {}
}
