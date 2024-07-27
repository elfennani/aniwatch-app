package com.elfennani.aniwatch.data.local.entities

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

@Entity(tableName = "watching_shows")
data class WatchingShowsDto(
    @PrimaryKey val id: Int,
    val name: String,
    val status: ShowStatus?,
    val description: String,
    val episodes: Int,
    val progress: Int?,
    @Embedded val image: WatchingShowImage
)

data class WatchingShowImage(
    val large: String,
    val medium: String,
    val original: String,
    val color: String?
)

fun WatchingShowsDto.toDomain() = ShowBasic(
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
    )
)

fun NetworkShowBasic.toDto() = WatchingShowsDto(
    id = id,
    name = name,
    status = status?.toDomain(),
    description = description,
    episodes = episodes,
    progress = progress,
    image = WatchingShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toColor()?.toHexString()
    )
)