package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.ShowDetails
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerializableShowDetails(
    val id: Int,
    val allanimeId: String,
    val name: String,
    val description: String,
    val episodesCount: Int,
    val episodes: List<NetworkEpisode>,
    val genres: List<String>,
    val tags: List<SerializableTag>,
    val season: SerializableShowSeason,
    val year: Int,
    val format: String,
    val image: SerializableShowImage,

    val banner: String?,
    val progress: Int?,
    val status: SerializableShowStatus?,
)

fun SerializableShowDetails.toDomain() = ShowDetails(
    id = this.id,
    name = this.name,
    tags = this.tags.map { it.toDomain() },
    year = this.year,
    image = this.image.toDomain(),
    banner = this.banner,
    format = this.format,
    genres = this.genres,
    season = this.season.toDomain(),
    status = this.status?.toDomain(),
    progress = this.progress,
    episodes = this.episodes.map { it.toDomain() },
    episodesCount = this.episodesCount,
    allanimeId = this.allanimeId,
    description = this.description
)
