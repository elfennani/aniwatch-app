package com.elfennani.aniwatch.data_old.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowImage
import com.elfennani.aniwatch.domain.models.ShowSeason
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.models.Tag
import com.elfennani.aniwatch.utils.toColor
import com.elfennani.aniwatch.utils.toHexString

@Entity(tableName = "cached_shows")
data class CachedShowDto(
    @PrimaryKey val id: Int,
    val allanimeId: String,
    val name: String,
    val description: String,
    val episodesCount: Int,
    val genres: List<String>,
    val season: ShowSeason,
    val year: Int,
    val format: String,
    @Embedded val image: CachedShowImage,
    val banner: String?,
    val progress: Int?,
    val status: ShowStatus?,
    @ColumnInfo(defaultValue = "[]") val tags: List<Tag>
)



fun CachedShowDto.toDomain() = ShowDetails(
    id = id,
    allanimeId = allanimeId,
    name = name,
    description = description,
    episodesCount = episodesCount,
    genres = genres,
    season = season,
    year = year,
    format = format,
    image = ShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toColor()
    ),
    banner = banner,
    progress = progress,
    status = status,
    tags = tags,
    episodes = emptyList()
)

fun ShowDetails.asEntity() = CachedShowDto(
    id = id,
    allanimeId = allanimeId,
    name = name,
    description = description,
    episodesCount = episodesCount,
    genres = genres,
    season = season,
    year = year,
    format = format,
    image = CachedShowImage(
        large = image.large,
        medium = image.medium,
        original = image.original,
        color = image.color?.toHexString()
    ),
    banner = banner,
    progress = progress,
    status = status,
    tags = tags
)