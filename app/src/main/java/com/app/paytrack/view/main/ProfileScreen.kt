package com.app.paytrack.view.main

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit,
) {

    Text(
        modifier = Modifier.clickable {
            onSignOutClick()
        },
        text = "Sign out"
    )

}