package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.data.local.entities.ActivityEntity
import com.elfennani.aniwatch.data.local.entities.CachedActivityShow
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.ActivityType
import com.elfennani.aniwatch.models.ActivtyShow
import com.elfennani.aniwatch.models.MediaType


fun ActivityEntity.asDomain() = Activity(
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

fun Activity.asEntity() = ActivityEntity(
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