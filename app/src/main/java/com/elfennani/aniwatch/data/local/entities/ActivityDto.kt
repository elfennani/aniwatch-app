package com.elfennani.aniwatch.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.ActivityType
import com.elfennani.aniwatch.models.ActivtyShow
import com.elfennani.aniwatch.models.MediaType
import java.util.Date

@Entity(tableName = "feed")
data class ActivityDto(
    @PrimaryKey(autoGenerate = true) val cachedId: Long? = null,
    val id: Int,
    val content: String,
    val type: String,
    val userId: Int,
    val userName: String,
    val userAvatar: String,
    val createdAt: Date,
    val likes: Int,
    val replies: Int,
    @Embedded(prefix = "show_") val show: CachedActivityShow?,
)

data class CachedActivityShow(
    val id: Int,
    val name: String,
    val image: String,
    val type: String,
    val year: Int?,
)

fun ActivityDto.asDomain() = Activity(
    id = id,
    content = content,
    type = ActivityType.valueOf(type),
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    createdAt = createdAt,
    likes = likes,
    replies = replies,
    show = if(show != null) ActivtyShow(
        id = show.id,
        name = show.name,
        image = show.image,
        type = MediaType.valueOf(show.type),
        year = show.year
    ) else null
)

fun Activity.asEntity() = ActivityDto(
    id = id,
    content = content,
    type = type.name,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    createdAt = createdAt,
    likes = likes,
    replies = replies,
    show = if(show != null) CachedActivityShow(
        id = show.id,
        name = show.name,
        image = show.image,
        type = show.type.name,
        year = show.year
    ) else null
)