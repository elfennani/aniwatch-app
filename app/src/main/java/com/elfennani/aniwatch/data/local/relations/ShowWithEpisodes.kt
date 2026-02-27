package com.elfennani.aniwatch.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.aniwatch.data.local.entities.ShowEntity
import com.elfennani.aniwatch.data.local.entities.EpisodeEntity
import com.elfennani.aniwatch.data.local.entities.DownloadedEpisodeEntity
import com.elfennani.aniwatch.data.local.mappers.toDomain

data class ShowWithEpisodes(
    @Embedded val show: ShowEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "animeId"
    )
    val episodes: List<EpisodeEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "showId"
    )
    val downloadedEpisodes: List<DownloadedEpisodeEntity>,
)

