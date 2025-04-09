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
                            val userRef =
                                fireStore.collection(Collections.Users.value).document(userDocId)

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


    suspend fun updateBalance(
        userEmail: String,
        accountId: String,
        amount: Double,
        type: String
    ): Boolean {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userDoc = querySnapshot.documents[0]
                            val documentId = userDoc.id

                            // Fetch the current balance
                            fireStore.collection(Collections.Users.value)
                                .document(documentId)
                                .collection("accounts")
                                .document(accountId)
                                .get()
                                .addOnSuccessListener { accountSnapshot ->
                                    val currentBalance = accountSnapshot.getDouble("balance") ?: 0.0

                                    // Add or subtract based on type
                                    val updatedBalance = if (type == "0") {
                                        currentBalance + amount
                                    } else {
                                        currentBalance - amount
                                    }

                                    // Update the balance
                                    fireStore.collection(Collections.Users.value)
                                        .document(documentId)
                                        .collection("accounts")
                                        .document(accountId)
                                        .update("balance", updatedBalance)
                                        .addOnSuccessListener {
                                            if (continuation.isActive) continuation.resume(
                                                true,
                                                null
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            if (continuation.isActive) continuation.resume(
                                                false,
                                                null
                                            )
                                        }

                                }
                                .addOnFailureListener { e ->
                                    if (continuation.isActive) continuation.resume(false, null)
                                }

                        } else {
                            if (continuation.isActive) continuation.resume(false, null)
                        }
                    }
                    .addOnFailureListener { e ->
                        if (continuation.isActive) continuation.resumeWithException(e)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            false
        }
    }


    suspend fun updateCategory(category: String) {

    }

    suspend fun getBalance(email: String, accountId: String): Double {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userDoc = querySnapshot.documents[0]
                            val documentId = userDoc.id

                            fireStore.collection(Collections.Users.value)
                                .document(documentId)
                                .collection("accounts")
                                .document(accountId)
                                .get()
                                .addOnSuccessListener { accountSnapshot ->
                                    val balanceRaw = accountSnapshot.get("balance") as? List<Map<String, Any>>
                                    val balanceList = balanceRaw?.mapNotNull {
                                        (it["balance"] as? Number)?.toDouble()
                                    } ?: emptyList()

                                    if (balanceList.isNotEmpty()) {
                                        val balance = balanceList[0]
                                        if (continuation.isActive) continuation.resume(balance, null)
                                    } else{
                                        if (continuation.isActive) continuation.resume(0.0, null)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    println("Error fetching account: ${e.message}")
                                    if (continuation.isActive) continuation.resume(0.0, null)
                                }
                        } else {
                            println("User not found")
                            if (continuation.isActive) continuation.resume(0.0, null)
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Error finding user: ${e.message}")
                        if (continuation.isActive) continuation.resume(0.0, null)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("Error getting balance: ${e.message}")
            0.0
        }
    }



}