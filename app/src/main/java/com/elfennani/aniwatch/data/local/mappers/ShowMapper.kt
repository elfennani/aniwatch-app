package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.data.local.entities.EmbeddedShowImage
import com.elfennani.aniwatch.data.local.entities.ShowEntity
import com.elfennani.aniwatch.data.local.relations.ShowWithEpisodes
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.utils.toColor
import com.elfennani.aniwatch.utils.toHexString

fun ShowEntity.toDomain() = ShowDetails(
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

fun ShowDetails.asEntity() = ShowEntity(
    id = id,
    allanimeId = allanimeId,
    name = name,
    description = description,
    episodesCount = episodesCount,
    genres = genres,
    season = season,
    year = year,
    format = format,
    image = EmbeddedShowImage(
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

fun ShowWithEpisodes.toDomain() =
    show.toDomain().copy(
        episodes = episodes
            .map { episode ->
                val downloaded = downloadedEpisodes.find { it.episode == episode.episode }
                episode.toDomain(downloaded)
            }
    )