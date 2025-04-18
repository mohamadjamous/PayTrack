package com.app.paytrack.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.view.components.BackButton
import com.app.paytrack.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton {
                onBackClick()
            }

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Notification toggle
        SettingToggleItem(
            icon = Icons.Outlined.Notifications,
            title = "Notifications",
            isChecked = notificationsEnabled,
            onToggle = { notificationsEnabled = it }
        )



        SettingItem(
            icon = Icons.Outlined.Share,
            title = "Share App"
        ) {
            // Implement share logic
        }

        // Dark mode toggle
        SettingToggleItem(
            icon = Icons.Outlined.Info,
            title = "Dark Mode",
            isChecked = darkModeEnabled,
            onToggle = { darkModeEnabled = it }
        )

        SettingItem(
            icon = Icons.Outlined.Lock,
            title = "Privacy Policy"
        ) {
            // Navigate to Privacy Policy
        }

        SettingItem(
            icon = Icons.Outlined.Info,
            title = "About App"
        ) {
            // Navigate or show dialog
        }
    }
}


@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
            .padding(top = 18.dp, bottom = 18.dp)
            .padding(horizontal = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.dark_green)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, fontSize = 16.sp)
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}


@Composable
fun SettingToggleItem(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
            .padding(top = 10.dp, bottom = 10.dp)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.dark_green)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, fontSize = 16.sp)
        }

        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = colorResource(id = R.color.green),
                uncheckedTrackColor = Color.LightGray,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}


@Preview(showSystemUi = true)
@Composable
fun SettingsScreenPreview(modifier: Modifier = Modifier) {
    SettingsScreen(
        onBackClick = {},
        viewModel = SettingsViewModel()
    )
}