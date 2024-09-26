package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.models.enums.ShowFormat
import com.elfennani.aniwatch.domain.models.enums.ShowSeason
import com.elfennani.aniwatch.domain.models.enums.ShowStatus

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
)