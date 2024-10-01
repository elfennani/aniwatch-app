package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.models.enums.MediaState
import com.elfennani.aniwatch.domain.models.enums.MediaType
import com.elfennani.aniwatch.domain.models.enums.ShowFormat
import com.elfennani.aniwatch.domain.models.enums.ShowSeason
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import kotlinx.datetime.Instant

data class Show(
    val id: Int,
    val name: String,
    val description: String?,
    val episodes: Int?,
    val genres: List<String>,
    val tags: List<Tag>,
    val season: ShowSeason?,
    val year: Int?,
    val format: ShowFormat,
    val image: ShowImage,
    val banner: String?,
    val progress: Int?,
    val status: ShowStatus?,
    val type: MediaType,
    val state: MediaState?,
    val score: Double?,
    val favorite: Boolean,
    val startedAt: StatusDate?,
    val endedAt:StatusDate?,
    val updatedAt: Instant?
)