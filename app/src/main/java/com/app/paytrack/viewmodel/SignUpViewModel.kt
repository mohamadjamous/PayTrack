package com.app.paytrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paytrack.model.User
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.view.components.SignInResult
import com.app.paytrack.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private val repo = AuthRepo()


    fun signUpUserEmailPassword(name: String, email: String, password: String) {


        // Create user account with email password
        viewModelScope.launch {

            // Check email exists in FireStore
            val exists = repo.checkEmailInAuth(email = email)

            // Reset state before starting sign-in process
            _state.value = SignInState()

            if (exists) {
                _state.value =
                    SignInState(isSignInSuccessful = false, signInError = "Email already exists!")
                return@launch
            } else {

                val isCreated = repo.createUser(email = email, password = password)

                if (isCreated) {

                    // Save user in FireStore
                    val isUserSaved =
                        repo.saveUser(User(name = name, email = email, isGoogleAccount = false))

                    if (isUserSaved) {
                        _state.value = SignInState(isSignInSuccessful = true)
                        return@launch
                    } else {
                        _state.value = SignInState(
                            isSignInSuccessful = false,
                            signInError = "Error Creating Account!"
                        )
                        return@launch
                    }

                } else {
                    _state.value = SignInState(
                        isSignInSuccessful = false,
                        signInError = "Error Creating Account!"
                    )
                    return@launch
                }
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

    fun onSignInCancelled() {
        _state.update { it.copy(isSignInCancelled = true) }
    }


    // This function is to save user account only when creating account with Google
    fun saveUser(email: String, name: String, onSuccess: () -> Unit, onFailure: () -> Unit) {

        viewModelScope.launch {
            // Attempt to save the user to the repository (or FireStore)
            val result = repo.saveUser(User(email = email, name = name, isGoogleAccount = true))

            // If the save operation is successful
            if (result) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }


}