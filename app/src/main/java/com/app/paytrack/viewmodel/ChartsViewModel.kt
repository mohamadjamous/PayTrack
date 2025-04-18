package com.app.paytrack.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.ChartsData
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChartsViewModel: ViewModel() {

    private val _data = MutableStateFlow(ChartsData())
    var data = _data.asStateFlow()

    private val repo = UserRepo()


    init {
        getChartsData()
    }

    private fun getChartsData() {

        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch
            _data.value = repo.getCategoriesData(email = userEmail).data!!
        }

    }


    fun resetState(){
        _data.value = ChartsData()
    }
}