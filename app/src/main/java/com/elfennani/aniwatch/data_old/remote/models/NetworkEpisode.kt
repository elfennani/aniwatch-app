package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.Episode
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkEpisode(
    val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Double,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Float?
)

fun NetworkEpisode.toDomain() = Episode(
    id = this.id,
    allanimeId = this.allanimeId,
    animeId = this.animeId,
    episode = this.episode,
    name = this.name,
    dubbed = this.dubbed,
    thumbnail = this.thumbnail,
    duration = this.duration?.toInt(),
    state = com.elfennani.aniwatch.domain.models.DownloadState.NotSaved
)