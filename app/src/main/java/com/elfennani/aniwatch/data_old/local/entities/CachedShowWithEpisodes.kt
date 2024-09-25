package com.elfennani.aniwatch.data_old.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CachedShowWithEpisodes(
    @Embedded val show: CachedShowDto,

    @Relation(
        parentColumn = "id",
        entityColumn = "animeId"
    )
    val episodes: List<CachedEpisodeDto>,

    @Relation(
        parentColumn = "id",
        entityColumn = "showId"
    )
    val downloadedEpisodes: List<LocalDownloadedEpisode>,
)

fun CachedShowWithEpisodes.toDomain() =
    show.toDomain().copy(
        episodes = episodes
            .map { episode ->
                val downloaded = downloadedEpisodes.find { it.episode == episode.episode }
                episode.toDomain(downloaded)
            }
    )