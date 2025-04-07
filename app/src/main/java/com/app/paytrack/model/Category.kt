package com.app.paytrack.model

import androidx.compose.ui.graphics.painter.Painter

data class Category(
    val painter: Painter,
    val name: String,
    val value: String
    )