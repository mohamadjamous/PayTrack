package com.app.paytrack.model

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {

    @Serializable
    object Welcome

    @Serializable
    object OnBoarding

    @Serializable
    object SignIn

    @Serializable
    object SignUp

    @Serializable
    object Home

    @Serializable
    object ForgotPassword

    @Serializable
    object VerifyOtp

    @Serializable
    object CreateAccount

    @Serializable
    object Profile

    @Serializable
    object Charts


}