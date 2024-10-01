package com.elfennani.aniwatch.data.local.models

import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.utils.toColor

data class EmbeddedCover(
    val extraLarge: String,
    val medium: String,
    val large: String,
    val color: String?,
)

fun EmbeddedCover.asAppModel() = ShowImage(
    large = large,
    medium = medium,
    original = extraLarge,
    color = color?.toColor()
)