package com.elfennani.aniwatch.domain.models

import java.util.Date

data class Activity(
    val id: Int,
    val content: String,
    val type: com.elfennani.aniwatch.domain.models.ActivityType,
    val userId: Int,
    val userName: String,
    val userAvatar: String,
    val createdAt: Date,
    val likes: Int,
    val replies: Int,
    val show: com.elfennani.aniwatch.domain.models.ActivtyShow?
)
