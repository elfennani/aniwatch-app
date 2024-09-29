package com.elfennani.aniwatch.data.local.models

import com.elfennani.aniwatch.domain.models.StatusDate

data class EmbeddedDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

fun EmbeddedDate.asAppModel() = StatusDate(
    year = year, month = month, day = day
)