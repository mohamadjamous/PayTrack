package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.view.sign_in.SignInResult
import com.app.paytrack.view.sign_in.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private val repo = AuthRepo()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }


    fun signInUserEmailPassword(email: String, password: String) {

        // Check email exists in FireStore

        // Create user account with email password
        viewModelScope.launch {
            val isLoggedIn = repo.loginUser(email = email, password = password)

            if (isLoggedIn) {
                // Save user in FireStore
                _state.value = SignInState(isSignInSuccessful = true)
            } else {
                _state.value = SignInState(isSignInSuccessful = false)
                _state.value = SignInState(signInError = "Something went wrong while signing in")
            }
        }

    }

    fun validateLoginInput(email: String, password: String): String? {
        if (email.isBlank()) {
            return "Email cannot be empty."
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email format."
        }
        if (password.isBlank()) {
            return "Password cannot be empty."
        }
        if (password.length < 6) {
            return "Password must be at least 6 characters long."
        }
        return null // Input is valid
    }


    fun sendVerificationLink(email: String){

        viewModelScope.launch {

            val result = repo.sendPasswordResetEmail(email)

            if (result){

                _state.value = SignInState(isPasswordLinkSuccessful = true)
            } else {
                _state.value = SignInState(isPasswordLinkSuccessful = false)
                _state.value = SignInState(passwordLinkError = "Something went wrong while sending link")
            }
        }

    }


}