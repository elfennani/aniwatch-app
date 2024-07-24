package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.models.Episode

@Entity("cached_episodes")
data class CachedEpisodeDto(
    @PrimaryKey val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Int,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?
)

fun CachedEpisodeDto.toDomain() = Episode(
    id = id,
    allanimeId = allanimeId,
    animeId = animeId,
    episode = episode,
    name = name,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration
)

fun Episode.toCached() = CachedEpisodeDto(
    id = id,
    allanimeId = allanimeId,
    animeId = animeId,
    episode = episode,
    name = name,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration
)