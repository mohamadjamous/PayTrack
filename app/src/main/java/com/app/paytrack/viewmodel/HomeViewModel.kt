package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.SignInState
import com.app.paytrack.model.UpdateBalanceState
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {


    private val _state = MutableStateFlow(UpdateBalanceState())
    val state = _state.asStateFlow()

    private val _stateBalance = MutableStateFlow(0.0)
    val balance = _stateBalance.asStateFlow()

    private val repo = UserRepo()


    init {
        getCurrentBalance()
    }


    private fun getCurrentBalance() {

        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            try {

                val result = repo.getBalance(email = userEmail, accountId = "0")
                _stateBalance.value = result

            } catch (e: Exception) {
                e.printStackTrace()
                // Log or notify UI of the error
                _stateBalance.value = 0.0 // or any fallback value
            }
        }
    }


    // update/create category with info like the amount, date, and other

    // update balance with negative or positive amount
    fun updateBalance(amount: String, type: String) {

        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch
            val result = repo.updateBalance(
                userEmail = userEmail,
                accountId = "0",
                amount = amount.toDouble(),
                type = type
            )

            _state.value = if (result) {
                UpdateBalanceState(success = true)
            } else {
                UpdateBalanceState(success = false, errorMessage = "Unable to update balance")
            }
        }
    }


}