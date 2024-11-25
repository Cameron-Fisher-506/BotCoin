package com.example.composecorelib.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composecorelib.R
import com.example.composecorelib.miscellaneous.DividerView

@Composable
fun OptionActionView(@DrawableRes drawableResourceId: Int, title: String, clickable: () -> Unit) {
    Surface(Modifier.wrapContentSize()) {
        Row(
            Modifier
                .fillMaxWidth(1f)
                .clickable { clickable() }
                .padding(15.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Image(
                painterResource(drawableResourceId),
                stringResource(R.string.information),
                Modifier.size(35.dp)
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                painterResource(R.drawable.ic_keyboard_arrow_right_black_24dp),
                stringResource(R.string.information)
            )
        }
        DividerView()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewView() {
    OptionActionView(R.drawable.ic_launcher_background, "Information") {
        
    }
}
