package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.User
import com.elfennani.aniwatch.domain.models.enums.ActivityType
import kotlinx.datetime.Instant

@Entity
data class LocalActivity(
    @PrimaryKey val id: Int,
    val content: String,
    val type: ActivityType,
    val userId: Int,
    val showId: Int?,
    val createdAt: Instant,
    val likes: Int,
    val replies: Int,
)

fun LocalActivity.asAppModel(user: User, show: Show?) = Activity(
    id = id,
    content = content,
    type = type,
    createdAt = createdAt,
    likes = 0,
    replies = 0,
    user = user,
    show = show
)