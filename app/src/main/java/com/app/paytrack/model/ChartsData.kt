package com.app.paytrack.model

data class ChartsData(
    val incomeData: List<CategoryData> = emptyList(),
    val expenseData: List<CategoryData> = emptyList(),
)