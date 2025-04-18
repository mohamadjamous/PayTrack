package com.app.paytrack.model.repo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.app.paytrack.R
import com.app.paytrack.model.Account
import com.app.paytrack.model.Category
import com.app.paytrack.model.CategoryData
import com.app.paytrack.model.ChartsData
import com.app.paytrack.model.Collections
import com.app.paytrack.model.MonthData
import com.app.paytrack.model.ProfileState
import com.app.paytrack.model.User
import com.app.paytrack.utlis.Resource
import com.app.paytrack.utlis.categories
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
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
                                    "date" to Calendar.getInstance().timeInMillis
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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMonthlyExpenseData(email: String): Resource<List<MonthData>> {
        return try {
            suspendCancellableCoroutine { continuation ->

                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        val userDoc = querySnapshot.documents.firstOrNull()

                        if (userDoc == null) {
                            continuation.resume(Resource.Error("User not found"), null)
                            return@addOnSuccessListener
                        }

                        val transactionsMap =
                            userDoc.get("transactions") as? Map<*, *> ?: emptyMap<Any, Any>()

                        val months = listOf(
                            "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"
                        )

                        val monthDataList = mutableListOf<MonthData>()

                        // Step 1: Group transactions by month
                        val monthlyTransactions =
                            months.associateWith { mutableListOf<Map<*, *>>() }

                        for ((_, value) in transactionsMap) {
                            val transaction = value as? Map<*, *> ?: continue
                            val dateMillis = (transaction["date"] as? Int)?.toLong() ?: continue
                            val date =
                                Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            val monthName = date.month.getDisplayName(
                                java.time.format.TextStyle.FULL,
                                Locale.ENGLISH
                            )

                            monthlyTransactions[monthName]?.add(transaction)
                        }

                        // Step 2: Process each month
                        var previousBalance = 0.0

                        months.forEachIndexed { index, month ->
                            val transactions = monthlyTransactions[month] ?: emptyList()

                            val dailyScores = MutableList(30) { 0 }

                            var balance = 0.0

                            for (txn in transactions) {

                                val amount = (txn["amount"] as? Number)?.toDouble() ?: continue
                                val type = (txn["type"] as? Number)?.toInt() ?: continue
                                val dateMillis = (txn["date"] as? Int)?.toLong() ?: continue
                                val date =
                                    Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault())
                                        .toLocalDate()

                                val dayIndex = (date.dayOfMonth - 1).coerceIn(0, 29)

                                // Type: 0 = income (+10), 1 = expense (-10)
                                when (type) {
                                    0 -> {
                                        balance += amount
                                        dailyScores[dayIndex] += 10
                                    }

                                    1 -> {
                                        balance -= amount
                                        dailyScores[dayIndex] -= 10
                                    }
                                }
                            }

                            // Step 3: Normalize to 0–100 (in 10s)
                            val normalizedSpendingData = dailyScores.map {
                                val percentage =
                                    (it + 100).coerceIn(0, 100) // Ensure no negative values
                                (percentage / 10) * 10
                            }

                            // Step 4: Calculate percentage change
                            val percentageChange = if (index > 0) {
                                val diff = balance - previousBalance
                                val percent =
                                    if (previousBalance != 0.0) (diff / previousBalance * 100) else 0.0
                                "${if (percent >= 0) "+" else ""}${String.format("%.1f", percent)}%"
                            } else {
                                "0%"
                            }

                            previousBalance = balance

                            monthDataList.add(
                                MonthData(
                                    id = index,
                                    name = month,
                                    spendingData = normalizedSpendingData,
                                    percentage = percentageChange,
                                    balance = balance
                                )
                            )
                        }

                        continuation.resume(Resource.Success(monthDataList), null)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }


    // Get Most Used Categories Data
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMostUsedCategories(email: String): Resource<List<Category>> {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val userDoc = querySnapshot.documents.firstOrNull()

                        if (userDoc == null) {
                            continuation.resume(Resource.Error("User not found"), null)
                            return@addOnSuccessListener
                        }

                        val transactionsMap =
                            userDoc.get("transactions") as? Map<*, *> ?: emptyMap<Any, Any>()

                        val categoryMeta = listOf(
                            Triple("Transport", R.drawable.transportation, 0),
                            Triple("Groceries", R.drawable.cart, 1),
                            Triple("Health", R.drawable.health, 2),
                            Triple("Shopping", R.drawable.basket, 3),
                            Triple("Gifts", R.drawable.gift, 4),
                            Triple("Entertainment", R.drawable.movie, 5)
                        )

                        val now = LocalDate.now()
                        val currentMonth = now.monthValue
                        val currentYear = now.year

                        val categoryUsage: Map<String, Double> = transactionsMap.values
                            .mapNotNull { it as? Map<*, *> }
                            .mapNotNull { transaction ->
                                val categoryName = transaction["categoryName"] as? String
                                val amount = (transaction["amount"] as? Number)?.toDouble()
                                    ?: return@mapNotNull null
                                val timestamp = (transaction["date"] as? Number)?.toLong()
                                    ?: return@mapNotNull null

                                val date = Instant.ofEpochMilli(timestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()

                                // Filter for current month and year
                                if (date.monthValue == currentMonth && date.year == currentYear && categoryName != null) {
                                    categoryName to amount
                                } else {
                                    null
                                }
                            }
                            .groupBy({ it.first }, { it.second })
                            .mapValues { entry -> entry.value.sum() }

                        val currentDate = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("MMMM d")
                        val formattedDate = currentDate.format(formatter)

                        val sortedCategories = categoryUsage.entries
                            .sortedByDescending { it.value }
                            .take(4)
                            .mapNotNull { (name, total) ->
                                val meta =
                                    categoryMeta.find { it.first == name } ?: return@mapNotNull null
                                Category(
                                    iconRes = meta.second,
                                    name = name,
                                    value = "$${total.toInt()}",
                                    date = formattedDate
                                )
                            }

                        continuation.resume(Resource.Success(sortedCategories), null)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCategories(email: String): Resource<List<Category>> {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val userDoc = querySnapshot.documents.firstOrNull()

                        if (userDoc == null) {
                            continuation.resume(Resource.Error("User not found"), null)
                            return@addOnSuccessListener
                        }

                        val transactionsMap =
                            userDoc.get("transactions") as? Map<*, *> ?: emptyMap<Any, Any>()

                        val categoryMeta = listOf(
                            Triple("Transport", R.drawable.transportation, 0),
                            Triple("Groceries", R.drawable.cart, 1),
                            Triple("Health", R.drawable.health, 2),
                            Triple("Shopping", R.drawable.basket, 3),
                            Triple("Gifts", R.drawable.gift, 4),
                            Triple("Entertainment", R.drawable.movie, 5)
                        )

                        val now = LocalDate.now()
                        val currentMonth = now.monthValue
                        val currentYear = now.year

                        val categoryUsage: Map<String, Double> = transactionsMap.values
                            .mapNotNull { it as? Map<*, *> }
                            .mapNotNull { transaction ->
                                val categoryName = transaction["categoryName"] as? String
                                val amount = (transaction["amount"] as? Number)?.toDouble()
                                    ?: return@mapNotNull null
                                val timestamp = (transaction["date"] as? Number)?.toLong()
                                    ?: return@mapNotNull null

                                val date = Instant.ofEpochMilli(timestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()

                                if (date.monthValue == currentMonth && date.year == currentYear && categoryName != null) {
                                    categoryName to amount
                                } else {
                                    null
                                }
                            }
                            .groupBy({ it.first }, { it.second })
                            .mapValues { entry -> entry.value.sum() }


                        val currentDate = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("MMMM d")
                        val formattedDate = currentDate.format(formatter)

                        val allCategories = categoryMeta.map { (name, iconRes, _) ->
                            val total = categoryUsage[name] ?: 0.0
                            Category(
                                iconRes = iconRes,
                                name = name,
                                value = "$${total.toInt()}",
                                date = formattedDate // e.g., "April 17"
                            )
                        }


                        continuation.resume(Resource.Success(allCategories), null)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCategoriesData(email: String): Resource<ChartsData> {
        return try {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(Collections.Users.value)
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->

                        val userDoc = querySnapshot.documents.firstOrNull()

                        if (userDoc == null) {
                            continuation.resume(Resource.Error("User not found"), null)
                            return@addOnSuccessListener
                        }

                        val now = LocalDate.now()
                        val currentMonth = now.monthValue
                        val currentYear = now.year

                        val transactionsMap = userDoc.get("transactions") as? Map<*, *> ?: emptyMap<Any, Any>()

                        val expenseMap = mutableMapOf<String, Int>()
                        val incomeMap = mutableMapOf<String, Int>()

                        for ((_, transaction) in transactionsMap) {
                            val trans = transaction as? Map<*, *> ?: continue

                            val timestamp = (trans["date"] as? Number)?.toLong() ?: continue

                            val date = Instant.ofEpochMilli(timestamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            if (date.monthValue != currentMonth || date.year != currentYear) continue

                            val type = (trans["type"] as? Number)?.toInt() ?: continue
                            val categoryName = (trans["categoryName"] as? String)?.takeIf { it.isNotBlank() } ?: "Other"

                            if (type == 1) {
                                // Expense → +10 per transaction
                                expenseMap[categoryName] = (expenseMap[categoryName] ?: 0) + 10
                            } else if (type == 0) {
                                // Income → just sum amount
                                val amount = (trans["amount"] as? Number)?.toInt() ?: continue
                                incomeMap[categoryName] = (incomeMap[categoryName] ?: 0) + amount
                            }
                        }

                        val expenseData = expenseMap.map { CategoryData(name = it.key, value = it.value) }
                        val incomeData = incomeMap.map { CategoryData(name = it.key, value = it.value) }

                        continuation.resume(Resource.Success(ChartsData(incomeData, expenseData)), null)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }




}