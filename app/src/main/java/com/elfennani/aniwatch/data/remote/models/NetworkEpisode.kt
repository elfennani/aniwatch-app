package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkEpisode(
    val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Int,
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
    state = DownloadState.NotSaved
)