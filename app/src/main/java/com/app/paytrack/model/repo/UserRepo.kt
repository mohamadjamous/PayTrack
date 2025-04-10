package com.app.paytrack.model.repo

import com.app.paytrack.model.Account
import com.app.paytrack.model.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
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
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        if (!querySnapshot.isEmpty) {

                            val userDoc = querySnapshot.documents[0]
                            val documentId = userDoc.id
                            var updatedBalance: Double

                            // Retrieve the 'accounts' field as a map from the user document
                            val accounts = userDoc.get("accounts") as? Map<String, Map<String, Any>>
                                ?: emptyMap()
                            val targetAccount = accounts[accountId]

                            // Getting the current balance
                            updatedBalance =
                                (targetAccount?.get("balance") as? Number)?.toDouble() ?: 0.0

                            // Update the current balance based on the transaction type
                            if (type == "0") {
                                updatedBalance += amount
                            } else {
                                updatedBalance -= amount
                            }


                            // Upload info
                            fireStore.collection(Collections.Users.value)
                                .document(documentId) // User document
                                .update(
                                    "accounts.$accountId.balance",
                                    updatedBalance
                                ) // Dot notation to target nested map
                                .addOnSuccessListener {

                                    if (continuation.isActive) continuation.resume(
                                        true,
                                        null
                                    )
                                }
                                .addOnFailureListener { e ->
                                    println("DebugError: ${e.message}")
                                    if (continuation.isActive) continuation.resume(
                                        false,
                                        null
                                    )
                                }

                        } else {
                            println("DebugError: User not found")
                            if (continuation.isActive) continuation.resume(false, null)
                        }
                    }
                    .addOnFailureListener { e ->
                        println("DebugError: ${e.message}")
                        if (continuation.isActive) continuation.resumeWithException(e)
                    }
            }
        } catch (e: Exception) {
            println("DebugError: ${e.message}")
            e.printStackTrace()
            if (e is CancellationException) throw e
            false
        }
    }


    suspend fun updateCategory(
        email: String,
        amount: Double,
        transactionType: Int,
        categoryId: String,
        categoryName: String,
        accountId: String,
        balanceBefore: Double,
        balanceAfter: Double
    ): Boolean {

        return try {
            suspendCancellableCoroutine { continuation ->

                fireStore
                    .collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

//                        val userDoc = querySnapshot.documents[0]
//
//                        // Retrieve the 'accounts' field as a map from the user document
//                        val accounts = userDoc.get("accounts") as? Map<String, Map<String, Any>> ?: emptyMap()
//                        val targetAccount = accounts[accountId]
//
//                        // Update or create category if it does not exist and add the transaction to the array inside the targetAccount map
//
//                        /*
//
//                        Category:
//
//                        id
//                        name
//                        transactions
//                        [
//                            0
//                                date
//                                amount
//                                type // should indicate if it's income or expense
//                                current balance
//
//                             1
//                                date
//                                amount
//                                type // should indicate if it's income or expense
//                                current balance
//                        ]
//
//                         */

                        val userDoc = querySnapshot.documents[0]

                        // Retrieve the 'accounts' field as a map from the user document
                        val accounts = userDoc.get("accounts") as? Map<String, Map<String, Any>> ?: emptyMap()
                        val targetAccount = accounts[accountId]

                        if (targetAccount != null) {

                            val userRef = fireStore.collection(Collections.Users.value).document(userDoc.id)

                            // Generate a unique ID for the transaction (same as your data class)
                            val transactionId = UUID.randomUUID().toString()

                            // Create the transaction map (matching your data class)
                            val transactionData = mapOf(
                                "id" to transactionId,
                                "amount" to amount,
                                "categoryName" to categoryName, // You may need to pass this in
                                "categoryId" to categoryId,
                                "balanceBefore" to balanceBefore,
                                "balanceAfter" to balanceAfter,
                                "accountId" to accountId,
                                "type" to transactionType,
                                "date" to System.currentTimeMillis().toInt() // Or Date().time.toInt()
                            )

                            // Define path to this transaction inside the category
                            val transactionPath = "transactions.$transactionId"

                            // Save the transaction by directly updating the path
                            userRef.update(transactionPath, transactionData)
                                .addOnSuccessListener {
                                    println("Transaction added successfully with ID $transactionId.")
                                    if (continuation.isActive) continuation.resume(true, null)
                                }
                                .addOnFailureListener { e ->
                                    println("Error adding transaction: ${e.message}")
                                    if (continuation.isActive) continuation.resume(false, null)
                                }
                        }

                        else {
                            if (continuation.isActive) continuation.resume(false, null)
                        }

                    }
                    .addOnFailureListener { e ->
                        println("ErrorFetchingUser: ${e.message}")
                        if (continuation.isActive) continuation.resume(false, null)
                    }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("ErrorGettingBalance: ${e.message}")
            false
        }
    }

    suspend fun getBalance(email: String, accountId: String): Double {

        return try {
            suspendCancellableCoroutine { continuation ->

                fireStore
                    .collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        val userDoc = querySnapshot.documents[0]

                        // Retrieve the 'accounts' field as a map from the user document
                        val accounts =
                            userDoc.get("accounts") as? Map<String, Map<String, Any>> ?: emptyMap()
                        val targetAccount = accounts[accountId]

                        if (targetAccount != null) {

                            val balance = (targetAccount["balance"] as? Number)?.toDouble() ?: 0.0
                            println("BalanceValue: $balance")

                            if (continuation.isActive) continuation.resume(balance, null)

                        } else {
                            println("Account with ID not found.")
                            if (continuation.isActive) continuation.resume(0.0, null)
                        }

                    }
                    .addOnFailureListener { e ->
                        println("ErrorFetchingUser: ${e.message}")
                    }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("ErrorGettingBalance: ${e.message}")
            0.0
        }
    }


}