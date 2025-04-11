package com.app.paytrack.model

data class ProfileState(
    val success: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null
)