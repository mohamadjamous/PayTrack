package com.app.paytrack.model.repo

import com.app.paytrack.model.Account
import com.app.paytrack.model.Collections
import com.app.paytrack.model.MonthData
import com.app.paytrack.model.ProfileState
import com.app.paytrack.model.User
import com.app.paytrack.utlis.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import java.time.ZoneId
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
                                    continuation.resumeWithException(e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Error querying user: ${e.message}")
                        continuation.resumeWithException(e)
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

                        if (querySnapshot != null && !querySnapshot.isEmpty) {

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

                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            val userDoc = querySnapshot.documents[0]

                            // Retrieve the 'accounts' field as a map from the user document
                            val accounts =
                                userDoc.get("accounts") as? Map<String, Map<String, Any>>
                                    ?: emptyMap()
                            val targetAccount = accounts[accountId]

                            if (targetAccount != null) {

                                val userRef = fireStore.collection(Collections.Users.value)
                                    .document(userDoc.id)

                                // Generate a unique ID for the transaction (same as your data class)
                                val transactionId = UUID.randomUUID().toString()

                                println("AmountValue: $amount")
                                println("TransactionType: $transactionType")
                                println("CategoryType: $categoryId")
                                println("FinalCategoryName: $categoryName")
                                println("BalanceAfter: $balanceAfter")
                                println("BalanceBefore: $balanceBefore")
                                println("CurrentDate: ${System.currentTimeMillis().toInt()}")

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
                                    "date" to System.currentTimeMillis().toInt()
                                )

                                // Define path to this transaction inside the category
                                val transactionPath = "transactions.$transactionId"

                                // Save the transaction by directly updating the path
                                userRef.update(transactionPath, transactionData)
                                    .addOnSuccessListener {
                                        println("Transaction added successfully with ID $transactionId.")
                                        continuation.resume(true, null)
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error adding transaction: ${e.message}")
                                        continuation.resume(false, null)
                                    }
                            } else {
                                continuation.resume(false, null)
                            }
                        } else {
                            continuation.resume(false, null)
                        }

                    }
                    .addOnFailureListener { e ->
                        println("ErrorFetchingUser: ${e.message}")
                        continuation.resume(false, null)
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

                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            val userDoc = querySnapshot.documents[0]

                            // Retrieve the 'accounts' field as a map from the user document
                            val accounts =
                                userDoc.get("accounts") as? Map<String, Map<String, Any>>
                                    ?: emptyMap()
                            val targetAccount = accounts[accountId]

                            if (targetAccount != null) {

                                val balance =
                                    (targetAccount["balance"] as? Number)?.toDouble() ?: 0.0
                                println("BalanceValue: $balance")

                                continuation.resume(balance, null)

                            } else {
                                println("Account with ID not found.")
                                continuation.resume(0.0, null)
                            }
                        } else {
                            println("Account with ID not found.")
                            continuation.resume(0.0, null)
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


    suspend fun fetchUserAccount(email: String): ProfileState {

        return try {
            suspendCancellableCoroutine { continuation ->

                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        if (querySnapshot != null && !querySnapshot.isEmpty) {

                            val userDoc = querySnapshot.documents[0]

                            val name = userDoc.get("name") as String
                            val email = userDoc.get("email") as String

                            val user = User(name = name, email = email, isGoogleAccount = false)

                            val state = ProfileState(
                                success = true,
                                user = user
                            )

                            continuation.resume(state, null)

                        } else {

                            val state = ProfileState(
                                success = false,
                                errorMessage = "Error getting user account"
                            )

                            continuation.resume(state, null)
                        }
                    }
                    .addOnFailureListener { e ->

                        val state = ProfileState(
                            success = false,
                            errorMessage = e.message
                        )

                        continuation.resume(state, null)
                        println("ErrorFetchingUser: ${e.message}")

                    }

            }

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println("ErrorGettingBalance: ${e.message}")
            val state = ProfileState(
                success = false,
                errorMessage = e.message
            )
            state
        }
    }


    suspend fun deleteAccount(email: String): Boolean {
        val user = FirebaseAuth.getInstance().currentUser ?: return false

        return try {
            suspendCancellableCoroutine { continuation ->
                // Step 1: Try to delete from Firebase Auth first
                user.delete()
                    .addOnSuccessListener {
                        // Step 2: After auth deletion, delete Firestore document
                        FirebaseFirestore.getInstance()
                            .collection(Collections.Users.value)
                            .whereEqualTo("email", email)
                            .limit(1)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val docId = snapshot.documents.firstOrNull()?.id
                                if (docId != null) {
                                    FirebaseFirestore.getInstance()
                                        .collection(Collections.Users.value).document(docId)
                                        .delete()
                                        .addOnSuccessListener {
                                            println("User document deleted.")
                                            continuation.resume(true, null)
                                        }
                                        .addOnFailureListener { e ->
                                            println("Auth deleted, but Firestore deletion failed: ${e.message}")
                                            continuation.resume(false, null)
                                        }
                                } else {
                                    println("No Firestore doc found.")
                                    continuation.resume(
                                        true,
                                        null
                                    ) // Auth deleted, no doc to delete
                                }
                            }
                            .addOnFailureListener {
                                println("Auth deleted, but Firestore lookup failed: ${it.message}")
                                continuation.resume(false, null)
                            }
                    }
                    .addOnFailureListener { e ->
                        println("Failed to delete user from Auth: ${e.message}")
                        continuation.resume(false, null)
                    }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }


    // Get Monthly Expenses Data

    /*
        Example Data

        // Represents all the year months
         val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
        )


        // This should represent how much money was added on that day, so the higher income after deducting all expense the higher the number, reaching to 100 not more than that
        val monthlySpendingData = remember {
            months.associateWith { List(30) { (0..100).random() } }
        }

     */
//    suspend fun getMonthlyExpenseData(email: String): Resource<List<MonthData>> {
//        return try {
//            suspendCancellableCoroutine { continuation ->
//
//                fireStore.collection(Collections.Users.value)
//                    .whereEqualTo("email", email)
//                    .limit(1)
//                    .get()
//                    .addOnSuccessListener { querySnapshot ->
//
//                        val userDoc = querySnapshot.documents.firstOrNull()
//
//                        if (userDoc == null) {
//                            continuation.resume(Resource.Error("User not found"), null)
//                            return@addOnSuccessListener
//                        }
//
//                        val transactionsMap =
//                            userDoc.get("transactions") as? Map<*, *> ?: emptyMap<Any, Any>()
//
//                        val months = listOf(
//                            "January", "February", "March", "April", "May", "June",
//                            "July", "August", "September", "October", "November", "December"
//                        )
//
//                        val monthlyBalanceMap = months.associateWith { MutableList(30) { 0.0 } }
//
//                        for ((_, value) in transactionsMap) {
//                            val transaction = value as? Map<*, *> ?: continue
//                            val type = (transaction["type"] as? Long)?.toInt() ?: continue
//                            val amount = (transaction["amount"] as? Double) ?: continue
//
//                            val dateMillis = (transaction["date"] as? Int)?.toLong() ?: continue
//                            val dateTime =
//                                Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault())
//
//                            val monthIndex = dateTime.monthValue - 1
//                            val dayIndex = (dateTime.dayOfMonth - 1).coerceIn(0, 29)
//                            val monthName = months.getOrNull(monthIndex) ?: continue
//
//                            when (type) {
//                                0 -> monthlyBalanceMap[monthName]?.let { it[dayIndex] += amount } // Income
//                                1 -> monthlyBalanceMap[monthName]?.let { it[dayIndex] -= amount } // Expense
//                            }
//                        }
//
//                        val monthlyDataList = months.map { month ->
//
//                            val balances = monthlyBalanceMap[month] ?: List(30) { 0.0 }
//                            val max = balances.maxOrNull()?.takeIf { it > 0 } ?: 1.0
//                            val spendingData = balances.map {
//                                val percentage = (it / max * 100).coerceIn(0.0, 100.0)
//                                (percentage / 10).toInt() * 10
//                            }
//                            val balance = balances.sum()
//                            val allBalances = monthlyBalanceMap.values.map { it.sum() }
//                            val percentage = String.format("%.2f", allBalances).toDouble()
//
//                            val percentStr: String = if (percentage > 0) {
//                                "+${percentage}%"
//                            } else if (percentage < 0) {
//                                "-${percentage}%"
//                            } else {
//                                "${percentage}%"
//                            }
//
//                            MonthData(
//                                name = month,
//                                spendingData = spendingData,
//                                percentage = percentStr,
//                                balance = balance
//                            )
//                        }
//
//                        continuation.resume(
//                            Resource.Success(
//                                MonthlyExpense(
//                                    months = months,
//                                    monthlyData = monthlyDataList
//                                )
//                            ),
//                            null
//                        )
//                    }
//                    .addOnFailureListener { continuation.resumeWithException(it) }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Error(e.message)
//        }
//    }


    // Get Most Used Categories Data


    // Get Expense Categories Data

}