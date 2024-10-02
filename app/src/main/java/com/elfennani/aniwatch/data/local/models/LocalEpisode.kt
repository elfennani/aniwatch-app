package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Episode

@Entity
data class LocalEpisode(
    @PrimaryKey val id: String,
    val showId: Int,
    val episode: Double,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
    val title: String,
)

fun LocalEpisode.asAppModel() = Episode(
    id= id,
    episode = episode,
    title = title,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration
)