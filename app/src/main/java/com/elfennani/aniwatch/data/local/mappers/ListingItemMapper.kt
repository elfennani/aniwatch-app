package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.data.local.entities.EmbeddedShowImage
import com.elfennani.aniwatch.data.local.entities.ListingItemEntity
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.utils.toColor
import com.elfennani.aniwatch.utils.toHexString

fun ListingItemEntity.toDomain() = ShowBasic(
    id = id,
    name = name,
    status = status,
    description = description,
    episodes = episodes,
    progress = progress,
    image = ShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toColor()
    ),
    banner = banner,
    updatedAt = updatedAt
)

fun NetworkShowBasic.toDto() = ListingItemEntity(
    id = id,
    name = name,
    status = status?.toDomain(),
    description = description,
    episodes = episodes,
    progress = progress,
    image = EmbeddedShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toColor()?.toHexString()
    ),
    banner = banner,
    updatedAt = updatedAt
)