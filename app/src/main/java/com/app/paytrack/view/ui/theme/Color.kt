package com.app.paytrack.view.ui.theme

import androidx.compose.ui.graphics.Color

val Green = Color(0xFF26994F)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val DarkGreen = Color(0xFF002F21)
val DarkGreen1 = Color(0xFF26994F)
val Gray = Color(0xFF7C7C7C)
val Divider = Color(0xFF4C5777)
val Cyan = Color(0xFF6CEB99)


sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color
) {
    object Night : ThemeColors(
        background = DarkGreen,
        surface = Black,
        primary = Green,
        text = White
    )

    object Day : ThemeColors(
        background = White,
        surface = Gray,
        primary = Green,
        text = Black
    )
}