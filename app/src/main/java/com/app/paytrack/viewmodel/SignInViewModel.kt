package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.view.components.SignInResult
import com.app.paytrack.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private val repo = AuthRepo()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage,
                isGoogleSignIn = result.data?.isGoogleSignIn ?: false
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }


    fun signInUserEmailPassword(email: String, password: String) {

        viewModelScope.launch {
            try {
                // Reset state before starting sign-in process
                _state.value = SignInState()

                val exists = repo.checkEmailExists(email)
                if (!exists) {
                    _state.value =
                        SignInState(signInError = "Email address or password is incorrect")
                    return@launch
                }

                val isLoggedIn = repo.loginUser(email, password)
                _state.value = SignInState(isSignInSuccessful = isLoggedIn)

                if (!isLoggedIn) {
                    _state.value =
                        SignInState(signInError = "Something went wrong while signing in")
                }

            } catch (e: Exception) {
                _state.value =
                    SignInState(signInError = e.localizedMessage ?: "Unknown error occurred")
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


    fun sendVerificationLink(email: String) {

        viewModelScope.launch {

            // Check email exists in FireStore


            // Reset state before starting sign-in process
            _state.value = SignInState()
            

            val result = repo.sendPasswordResetEmail(email)

            if (result) {

                _state.value = SignInState(isPasswordLinkSuccessful = true)
            } else {
                _state.value = SignInState(isPasswordLinkSuccessful = false)
                _state.value =
                    SignInState(passwordLinkError = "Something went wrong while sending link")
            }

        }

    }


    fun onSignInCancelled() {
        _state.update { it.copy(isSignInCancelled = true) }
    }


}