package com.elfennani.aniwatch.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CachedShowWithEpisodes(
    @Embedded val show: CachedShowDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "animeId"
    )
    val episodes: List<CachedEpisodeDto>
)

fun CachedShowWithEpisodes.toDomain() =
    show.toDomain().copy(episodes = episodes.map(CachedEpisodeDto::toDomain))