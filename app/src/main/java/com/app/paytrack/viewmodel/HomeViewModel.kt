package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.UpdateBalanceState
import com.app.paytrack.model.categories
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {


    private val _state = MutableStateFlow(UpdateBalanceState())
    val updateBalanceState = _state.asStateFlow()

    private val _stateBalance = MutableStateFlow(0.0)
    val balanceState = _stateBalance.asStateFlow()

    private val repo = UserRepo()
    private var currentBalance = 0.0


    init {
        getCurrentBalance()
    }


    fun getCurrentBalance() {

        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            try {

                val result = repo.getBalance(email = userEmail, accountId = "0")
                _stateBalance.value = result
                currentBalance = result

            } catch (e: Exception) {
                e.printStackTrace()
                // Log or notify UI of the error
                _stateBalance.value = 0.0 // or any fallback value
            }
        }
    }




    // update balance with negative or positive amount
    fun updateBalance(amount: String, type: String, categoryName: String?) {

        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            val result = repo.updateBalance(
                userEmail = userEmail,
                accountId = "0", // Static input
                amount = amount.toDouble(),
                type = type
            )

            if (result) {

                // Income, it does not have a category id
                if (type == "0") {

                    updateCategory(
                        amount = amount.toDouble(),
                        transactionType = type.toInt(),
                        categoryId = "",
                        categoryName = ""
                    )

                } else {
                    val targetCategory = categories.find { it.first == categoryName }
                    // Get selected category id
                    val id = targetCategory?.third

                    updateCategory(
                        amount = amount.toDouble(),
                        transactionType = type.toInt(),
                        categoryId = id.toString(),
                        categoryName = categoryName!!
                    )
                }

            }

            _state.value = if (result) {
                UpdateBalanceState(success = true)
            } else {
                UpdateBalanceState(success = false, errorMessage = "Unable to update balance")
            }
        }
    }

    // update/create category with info like the amount, date, and other
    private fun updateCategory(amount: Double, transactionType: Int, categoryId: String, categoryName: String) {

        // Calculate balance after
        val balanceAfter: Double = if (transactionType == 0){
            // Income
            currentBalance + amount
        }else{
            // Expense
            currentBalance - amount
        }


        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            val result = repo.updateCategory(
                email = userEmail,
                amount = amount,
                transactionType = transactionType,
                categoryId = categoryId,
                accountId = "0", // Static needs to be changed
                balanceBefore = currentBalance,
                balanceAfter = balanceAfter,
                 categoryName = categoryName
            )

            if (result) {
                println("Category was added or updated")
            } else {
                println("Error updating category")
            }
        }
    }


}