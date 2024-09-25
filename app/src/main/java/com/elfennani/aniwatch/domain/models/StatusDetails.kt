package com.elfennani.aniwatch.domain.models

data class StatusDetails(
    val status: ShowStatus?,
    val score: Int,
    val progress: Int,
    val favorite: Boolean,
    val startedAt: StatusDate?,
    val completedAt: StatusDate?,
)