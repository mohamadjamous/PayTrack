package com.app.paytrack.model

data class MonthlyExpense(
    val success: Boolean = false,
    val months: List<String> = emptyList(),
    val monthlySpendingData: Map< String, List<Int>> = months.associateWith { List(30) { 0 } },
    val monthlyPercentage: Double = 0.0,
    val positive: Boolean = false
)
