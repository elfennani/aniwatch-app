package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.Episode

data class SerializableEpisode(
    val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Int,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?
)

fun SerializableEpisode.toDomain() = Episode(
    id = this.id,
    allanimeId = this.allanimeId,
    animeId = this.animeId,
    episode = this.episode,
    name = this.name,
    dubbed = this.dubbed,
    thumbnail = this.thumbnail,
    duration = this.duration
)