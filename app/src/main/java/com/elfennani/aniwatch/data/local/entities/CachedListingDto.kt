package com.elfennani.aniwatch.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.toColor
import com.elfennani.aniwatch.toHexString

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