package com.elfennani.aniwatch.data_old.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.data_old.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data_old.remote.models.toDomain
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.utils.toColor
import com.elfennani.aniwatch.utils.toHexString

@Entity(tableName = "cached_listing")
data class CachedListingDto(
    @PrimaryKey val id: Int,
    val name: String,
    val status: ShowStatus?,
    val description: String?,
    val episodes: Int?,
    val progress: Int?,
    @Embedded val image: CachedShowImage,
    val banner: String? = null,
    val updatedAt: Int?
)

fun CachedListingDto.toDomain() = ShowBasic(
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

fun NetworkShowBasic.toDto() = CachedListingDto(
    id = id,
    name = name,
    status = status?.toDomain(),
    description = description,
    episodes = episodes,
    progress = progress,
    image = CachedShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toColor()?.toHexString()
    ),
    banner = banner,
    updatedAt = updatedAt
)