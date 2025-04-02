package com.app.paytrack.model.repo

import com.app.paytrack.model.Account
import com.app.paytrack.model.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepo {

    private val tag = "AuthRepository: "
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    suspend fun updateAccount(list: List<Account>): Boolean {
        return try {
            suspendCancellableCoroutine { continuation ->

                // Get the current user's email
                val userEmail = FirebaseAuth.getInstance().currentUser?.email

                if (userEmail.isNullOrEmpty()) {
                    continuation.resumeWithException(Exception("User email is null or empty"))
                    return@suspendCancellableCoroutine
                }

                // Query Firestore to find the document with the matching email
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            continuation.resumeWithException(
                                Exception("No user found with email $userEmail")
                            )
                        } else {
                            val userDoc = querySnapshot.documents.first()
                            val userDocId = userDoc.id

                            // Prepare the data for update
                            val accountsMap = list.mapIndexed { index, account ->
                                "accounts.$index" to account
                            }.toMap()

                            // Update the document using the found document ID
                            val userRef = fireStore.collection(Collections.Users.value).document(userDocId)

                            userRef.update(accountsMap)
                                .addOnSuccessListener {
                                    println("Accounts updated successfully")
                                    continuation.resume(true)
                                }
                                .addOnFailureListener { e ->
                                    println("Error updating accounts: ${e.message}")
                                    if (continuation.isActive) {
                                        continuation.resumeWithException(e)
                                    }
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Error querying user: ${e.message}")
                        if (continuation.isActive) {
                            continuation.resumeWithException(e)
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("Error updating accounts: ${e.message}")
            false
        }
    }


}