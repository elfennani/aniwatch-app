package com.elfennani.aniwatch

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.elfennani.aniwatch.data.remote.models.toDomain

fun Color.toHexString(): String {
    val colorInt = this.toArgb()

    val alpha = (colorInt shr 24) and 0xFF
    val red = (colorInt shr 16) and 0xFF
    val green = (colorInt shr 8) and 0xFF
    val blue = colorInt and 0xFF

    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

fun String.toColor(): Color = Color(android.graphics.Color.parseColor(this))