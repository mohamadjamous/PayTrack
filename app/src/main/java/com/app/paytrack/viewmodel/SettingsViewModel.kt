package com.app.paytrack.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.app.paytrack.model.repo.UserRepo
import com.app.paytrack.utlis.ReminderWorker
import com.app.paytrack.utlis.Resource
import com.app.paytrack.utlis.ThemePreference
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingsViewModel(
    private val context: Context
): ViewModel() {


    // notifications state
    private val _isReminderEnabled = MutableStateFlow(true)
    val isReminderEnabled: StateFlow<Boolean> = _isReminderEnabled.asStateFlow()

    val repo = UserRepo()


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




    fun loadReminderEnabled() {
        viewModelScope.launch {
            ThemePreference.getReminderToggle(context)
                .collect { enabled ->
                    _isReminderEnabled.value = enabled
                }
        }
    }

    fun onReminderToggled(enabled: Boolean) {

        val email = FirebaseAuth.getInstance().currentUser?.email ?: return

        viewModelScope.launch {
            val updateResult = repo.updateReminderEnabled(email, enabled)
            if (updateResult is Resource.Success) {
                ThemePreference.setReminderToggle(context, enabled)
                _isReminderEnabled.value = enabled
                Toast.makeText(context, "Reminder setting updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show()
            }
        }
    }




}