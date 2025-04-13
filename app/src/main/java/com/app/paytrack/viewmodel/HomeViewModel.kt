package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.MonthlyExpense
import com.app.paytrack.model.UpdateBalanceState
import com.app.paytrack.model.categories
import com.app.paytrack.model.repo.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
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


    private val _monthlyExpenseState = MutableStateFlow(MonthlyExpense())
    val monthlyExpenseState = _monthlyExpenseState.asStateFlow()


    init {
        getCurrentBalance()
        getMonthlyData()
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
                val transactionType = type.toInt()
                val targetCategory = categories.find { it.first == categoryName }
                val categoryId = if (transactionType == 0) "" else targetCategory?.third.toString()
                val finalCategoryName = categoryName ?: ""

                val categoryResult = updateCategoryInternal(
                    amount = amount.toDouble(),
                    transactionType = transactionType,
                    categoryId = categoryId,
                    categoryName = finalCategoryName
                )

                // Only update state once at the end
                _state.value = if (categoryResult) {
                    UpdateBalanceState(success = true)
                } else {
                    UpdateBalanceState(success = false, errorMessage = "Failed to update category.")
                }
            } else {
                _state.value = UpdateBalanceState(success = false, errorMessage = "Unable to update balance.")
            }
        }
    }

    private suspend fun updateCategoryInternal(
        amount: Double,
        transactionType: Int,
        categoryId: String,
        categoryName: String
    ): Boolean {

        val balanceAfter = if (transactionType == 0) {
            currentBalance + amount
        } else {
            currentBalance - amount
        }

        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return false

        return repo.updateCategory(
            email = userEmail,
            amount = amount,
            transactionType = transactionType,
            categoryId = categoryId,
            accountId = "0", // Static input
            balanceBefore = currentBalance,
            balanceAfter = balanceAfter,
            categoryName = categoryName
        )
    }


    fun resetUpdateState() {
        _state.value = UpdateBalanceState()
    }



    fun getMonthlyData(){


        viewModelScope.launch {

            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return@launch

            _monthlyExpenseState.value = repo.getMonthlyExpenseData(email = userEmail)
        }
    }

    fun resetMonthlyState() {
        _monthlyExpenseState.value = MonthlyExpense(success = false)
    }


}