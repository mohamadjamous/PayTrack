package com.app.paytrack.model

data class MonthData(
    val id : Int,
    val name: String,
    val spendingData: List<Int>,
    val percentage: String,
    val balance: Double
)
