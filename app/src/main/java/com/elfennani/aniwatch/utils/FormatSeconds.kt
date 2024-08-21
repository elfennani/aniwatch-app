package com.elfennani.aniwatch.utils

import java.util.Locale

fun Int.formatSeconds(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return if (hours > 0) {
        String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format(Locale.ROOT, "%02d:%02d", minutes, remainingSeconds)
    }
}