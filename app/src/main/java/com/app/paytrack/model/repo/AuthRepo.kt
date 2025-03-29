package com.app.paytrack.model.repo

import com.app.paytrack.model.Collections
import com.app.paytrack.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepo {

    private val tag = "AuthRepository: "
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    fun isLoggedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            println(tag + "Already logged in")
            return true
        }

        return false
    }

    suspend fun createUser(email: String, password: String): Boolean {

        return try {
            val authResult = suspendCancellableCoroutine { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        println("Register success")
                        continuation.resume(true) // Resume with success
                    }
                    .addOnFailureListener {
                        println("Register failure: ${it.message}")
                        continuation.resume(false) // Resume with failure
                    }
            }

            if (authResult) {
                loginUser(email, password) // Waits for this to complete
            } else {
                false // Registration failed, so don't attempt login
            }

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("Register exception: ${e.message}")
            false
        }
    }


    suspend fun loginUser(
        email: String, password: String
    ): Boolean {
        try {

            val result = suspendCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        println(tag + "login success")
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        println(tag + "login failure ${it.message}")
                        continuation.resume(false)
                    }
            }

            return result

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "login exception ${e.message}")
            return false
        }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Boolean {
        try {

            val result = suspendCoroutine { continuation ->
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        println(tag + "password link success")
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        println(tag + "password link failure ${it.message}")
                        continuation.resume(false)
                    }
            }

            return result

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "password link exception ${e.message}")
            return false
        }
    }


    suspend fun saveUser(user: User): Boolean {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .add(user)
                    .addOnSuccessListener {
                        println("User saved successfully")
                        continuation.resume(true) // Resume with success
                    }
                    .addOnFailureListener {
                        println("Error saving user: ${it.message}")
                        if (continuation.isActive) {
                            continuation.resumeWithException(it) // Throw exception for proper error handling
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e // Properly handle coroutine cancellation
            println("Error saving user: ${e.message}")
            false
        }
    }

    suspend fun checkEmailInAuth(email: String): Boolean {
        return try {
            val result = FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).await()
            result.signInMethods?.isNotEmpty() == true // True if email exists
        } catch (e: Exception) {
            false // Handle errors gracefully
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            fireStore.collection(Collections.Users.value)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(!snapshot.isEmpty)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }

}