package com.example.composecorelib.informational

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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

@Composable
fun InformationViewCompose(title: String) {
    Surface(Modifier.wrapContentSize()) {
        Row(
            Modifier
                .fillMaxWidth(1f),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                title,
                Modifier.padding(20.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                painterResource(R.drawable.ic_info_black_24dp),
                stringResource(R.string.information),
                Modifier.padding(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    InformationViewCompose("Information")
}