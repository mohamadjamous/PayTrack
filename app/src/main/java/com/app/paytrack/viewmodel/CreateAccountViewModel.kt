package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.Account
import com.app.paytrack.model.AccountState
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CreateAccountViewModel : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()


    fun updateUserAccount(list: List<Account>) {

        val repo = UserRepo()

        viewModelScope.launch {

            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser?.uid == null) {
                _state.update {
                    AccountState(
                        isCreateAccountSuccess = false,
                        createAccountError = "User not logged in"
                    )
                }
                return@launch
            }

            val result = repo.updateAccount(
                list = list
            )

            if (result) {
                _state.update {
                    AccountState(
                        isCreateAccountSuccess = true,
                        createAccountError = null
                    )
                }
            } else {
                _state.update {
                    AccountState(
                        isCreateAccountSuccess = false,
                        createAccountError = "Failed to update account"
                    )
                }
            }
        }

    }


    fun resetState() {
        _state.update { AccountState() }
    }


}