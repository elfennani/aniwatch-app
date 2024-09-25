package com.elfennani.aniwatch.domain.models

import androidx.compose.ui.graphics.Color

data class ShowImage(
    val large: String,
    val medium: String,
    val original: String,
    val color: Color?
)
