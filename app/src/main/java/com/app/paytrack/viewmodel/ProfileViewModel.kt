package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.ProfileState
import com.app.paytrack.model.UpdateBalanceState
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {


    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    val repo = UserRepo()

    private val _deleteState = MutableStateFlow(-1)
    val deleteState = _deleteState.asStateFlow()

    init {
        fetchUserInfo()
    }


    private fun fetchUserInfo() {


        viewModelScope.launch {

            // Get user email
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            // Update state
            _state.value = UserRepo().fetchUserAccount(email = userEmail)

        }

    }

    fun deleteAccount() {

        viewModelScope.launch {

            // Get user email
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            if (!repo.deleteAccount(email = userEmail)){

                _deleteState.value = 0
            }else{

                // Success
                _deleteState.value = 1
            }

        }
    }


}