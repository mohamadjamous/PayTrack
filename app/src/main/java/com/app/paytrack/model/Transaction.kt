package com.app.paytrack.model

data class Transaction(
    val id: String,
    val amount: Double,
    val categoryName: String,
    val categoryId: String,
    val balanceBefore: Double,
    val balanceAfter: Double,
    val accountId : String,
    val type: Int,
    val date: Int
    )
