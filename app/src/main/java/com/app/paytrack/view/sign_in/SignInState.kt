package com.app.paytrack.view.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val isPasswordLinkSuccessful: Boolean = false,
    val passwordLinkError: String? = null,
)