package com.app.paytrack.model

data class AccountState(
    val isCreateAccountSuccess: Boolean = false,
    val createAccountError: String? = null
)