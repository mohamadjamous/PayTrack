package com.app.paytrack.model

data class Category(
    val iconRes: Int,
    val name: String,
    val desc: String = "",
    val date: String = "",
    val value: String
)