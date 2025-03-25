package com.app.paytrack.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    IconButton(
        modifier = modifier,
        onClick = { onClick() },

    ) {
        Icon(Icons.Filled.ArrowBack,
            contentDescription = "Back Button"
        )

    }
}


@Preview(showBackground = true)
@Composable
fun BackButtonPreview(modifier: Modifier = Modifier) {
    BackButton(
        onClick = {}
    )
}