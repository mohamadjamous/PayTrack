package com.app.paytrack.model.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepo {

    private val tag = "AuthRepository: "
    private val firebaseAuth = FirebaseAuth.getInstance()

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

    fun logout() {
        firebaseAuth.signOut()
    }

}