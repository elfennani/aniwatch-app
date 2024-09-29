package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.models.enums.ShowStatus

data class StatusDetails(
    val status: ShowStatus?,
    val score: Double,
    val progress: Int,
    val favorite: Boolean,
    val startedAt: StatusDate?,
    val completedAt: StatusDate?,
)