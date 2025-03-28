package com.app.paytrack.model.repo

import com.app.paytrack.model.Collections
import com.app.paytrack.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
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

    suspend fun createUser(
        email: String, password: String
    ): Boolean {
        try {

            val result = suspendCoroutine { continuation ->

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        println(tag + "register success")
                        CoroutineScope(Dispatchers.IO).launch {
                            continuation.resume(loginUser(email, password))
                        }
                    }
                    .addOnFailureListener {
                        println(tag + "register failure ${it.message}")
                        continuation.resume(false)
                    }

            }

            return result

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "register exception ${e.message}")
            return false
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


    suspend fun saveUser(
        user: User
    ): Boolean {
        try {

            val result = suspendCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .add(user)
                    .addOnSuccessListener {
                        println(tag + "user saved successfully")
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        println(tag + "error saving user ${it.message}")
                        continuation.resume(false)
                    }
            }

            return result

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "error saving user ${e.message}")
            return false
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }

}