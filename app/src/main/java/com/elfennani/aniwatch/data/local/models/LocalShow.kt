package com.elfennani.aniwatch.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.Tag
import com.elfennani.aniwatch.domain.models.enums.MediaState
import com.elfennani.aniwatch.domain.models.enums.MediaType
import com.elfennani.aniwatch.domain.models.enums.ShowFormat
import com.elfennani.aniwatch.domain.models.enums.ShowSeason
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import kotlinx.datetime.Instant

@Entity
data class LocalShow(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val genres: List<String>,
    val episodes: Int?,
    @Embedded("cover_") val cover: EmbeddedCover,
    val season: ShowSeason?,
    val year: Int?,
    val format: ShowFormat,
    val banner: String?,
    val progress:Int?,
    val status: ShowStatus?,
    val tags: List<Tag>,
    val score: Double?,
    val type:MediaType,
    val state: MediaState?,
    val favorite: Boolean,
    val updatedAt: Instant?,
    @Embedded("started_") val startedAt: EmbeddedDate?,
    @Embedded("ended_") val endedAt: EmbeddedDate?
)

fun LocalShow.asAppModel() = Show(
    id = id,
    name = title,
    description = description,
    episodes = episodes,
    genres = genres,
    tags = tags,
    season = season,
    year = year,
    format = format,
    image = cover.asAppModel(),
    banner = banner,
    progress = progress,
    status = status,
    type = type,
    state = state,
    score = score,
    favorite = favorite,
    startedAt = startedAt?.asAppModel(),
    endedAt = endedAt?.asAppModel(),
    updatedAt = updatedAt
)