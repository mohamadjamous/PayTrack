package com.app.paytrack.view.ui.theme

import androidx.compose.ui.graphics.Color

val Green = Color(0xFF26994F)
val Black = Color(0xFF000000)
val FadeBlack = Color(0xFF151514)
val White = Color(0xFFFFFFFF)
val DarkGreen = Color(0xFF002F21)
val Gray = Color(0xFF7C7C7C)


sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color,
    val container: Color
) {
    object Night : ThemeColors(
        background = FadeBlack,
        surface = Gray,
        primary = Green,
        text = White,
        container = DarkGreen
    )

    object Day : ThemeColors(
        background = White,
        surface = Gray,
        primary = Green,
        text = Black,
        container = DarkGreen
    )
}