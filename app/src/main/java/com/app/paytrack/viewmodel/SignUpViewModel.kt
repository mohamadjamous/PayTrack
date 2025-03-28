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

class SignUpViewModel : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private val repo = AuthRepo()


    fun signUpUserEmailPassword(name: String, email: String, password: String) {

        // Check email exists in FireStore

        // Create user account with email password
        viewModelScope.launch {
            val isCreated = repo.createUser(email = email, password = password)

            if (isCreated) {
                // Save user in FireStore
                _state.value = SignInState(isSignInSuccessful = true)
            } else {
                _state.value = SignInState(isSignInSuccessful = false)
                _state.value = SignInState(signInError = "Error Creating Account!")
            }
        }

    }


    fun validateInput(
        email: String,
        name: String,
        password: String,
        confirmPassword: String
    ): String? {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || name.isBlank()) {
            return "Fields cannot be empty"
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email format"
        }

        if (password != confirmPassword) {
            return "Passwords do not match"
        }

        return null // Validation passed
    }


    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

}