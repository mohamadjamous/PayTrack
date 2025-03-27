package com.app.paytrack.view.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit) {
    Column {

        Text(text = "Main Screen")

        Text(
            modifier = Modifier.clickable {
              onSignOutClick()
            },
            text = "Sign out")
    }
}