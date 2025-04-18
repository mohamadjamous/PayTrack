package com.app.paytrack.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {




    // dark mode state
    // notifications state



    fun shareApp(context: Context) {

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this awesome app!\nDownload it here: https://play.google.com/store/apps/details?id=${context.packageName}"
            )
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share app via"))
    }

    fun openPrivacyPolicy(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://app-privacy-policy-generator.firebaseapp.com/"))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No browser found to open the link", Toast.LENGTH_SHORT).show()
        }
    }

}