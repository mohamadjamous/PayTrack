package com.app.paytrack.model

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val isPasswordLinkSuccessful: Boolean = false,
    val passwordLinkError: String? = null,
    val isGoogleSignIn: Boolean = false,
    val isSignInCancelled: Boolean = false

)