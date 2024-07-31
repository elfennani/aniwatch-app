package com.elfennani.aniwatch

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.LayoutDirection
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.elfennani.aniwatch.data.remote.models.toDomain
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

fun Color.toHexString(): String {
    val colorInt = this.toArgb()

    val alpha = (colorInt shr 24) and 0xFF
    val red = (colorInt shr 16) and 0xFF
    val green = (colorInt shr 8) and 0xFF
    val blue = colorInt and 0xFF

    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

fun String.toColor(): Color = Color(android.graphics.Color.parseColor(this))

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

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
            other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
            other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)

fun Context.imageLoader() = ImageLoader.Builder(this)
    .memoryCache {
        MemoryCache.Builder(this)
            .maxSizePercent(0.25)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(this.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
    .crossfade(true)
    .build()

fun Context.requireActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No activity was present but it is required.")
}