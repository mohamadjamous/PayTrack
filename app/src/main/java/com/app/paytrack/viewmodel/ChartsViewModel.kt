package com.app.paytrack.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.ChartsData
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class ChartsViewModel : ViewModel() {

    private val _data = MutableStateFlow(ChartsData())
    val data = _data.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val repo = UserRepo()

    init {
        getChartsData()
    }

    private fun getChartsData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch
                val result = repo.getCategoriesData(email = userEmail)
                result.data?.let {
                    _data.value = it
                }
            } catch (e: Exception) {
                // Handle errors if needed
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetState() {
        _data.value = ChartsData()
    }
}
