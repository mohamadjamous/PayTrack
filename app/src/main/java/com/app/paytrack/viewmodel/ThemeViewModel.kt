package com.app.paytrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.utlis.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    init {
        viewModelScope.launch {
            ThemePreference.getDarkModeFlow(application.applicationContext)
                .collect { _isDarkTheme.value = it }
        }
    }

    fun toggleTheme() {
        val newTheme = !_isDarkTheme.value
        viewModelScope.launch {
            ThemePreference.setDarkMode(getApplication(), newTheme)
        }
    }
}
