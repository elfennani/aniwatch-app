package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.models.enums.ActivityType
import kotlinx.datetime.Instant

data class Activity(
    val id: Int,
    val content: String,
    val type: ActivityType,
    val createdAt: Instant,
    val likes: Int,
    val replies: Int,
    val user: User,
    val show: Show?
)
