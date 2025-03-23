package com.app.paytrack.model

sealed class Screen(val route: String) {

    data object Welcome: Screen(route = "welcome_screen")
    data object OnBoarding: Screen(route = "onboarding_screen")
    data object SignIn: Screen(route = "signin_screen")
    data object SignUp: Screen(route = "signin_screen")
}