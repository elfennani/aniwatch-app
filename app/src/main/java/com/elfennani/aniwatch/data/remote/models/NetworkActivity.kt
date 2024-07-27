package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.ActivityType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class NetworkActivity(
    val id: Int,
    val content: String,
    val type: ActivityType,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "user_name") val userName: String,
    @Json(name = "user_avatar") val userAvatar: String,
    @Json(name = "created_at") val createdAt: Long,
    val likes: Int,
    val replies: Int,
    val show: NetworkActivityShow?
)

fun NetworkActivity.asDomain() = Activity(
    id = id,
    content = content,
    type = type,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    createdAt = Date(createdAt*1000L),
    likes = likes,
    replies = replies,
    show = show?.asDomain()
)