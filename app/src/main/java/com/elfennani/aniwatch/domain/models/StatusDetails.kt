package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus

data class StatusDetails(
    val status: ShowStatus?,
    val score: Int,
    val progress: Int,
    val favorite: Boolean,
    val startedAt: StatusDate?,
    val completedAt: StatusDate?,
)