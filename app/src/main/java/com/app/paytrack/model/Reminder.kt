package com.app.paytrack.model

data class Reminder(
    val message: String,
    val timestamp: Long,
    val enabled: Boolean
)
