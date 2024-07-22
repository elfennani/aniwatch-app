package com.elfennani.aniwatch.domain.models

data class ShowDetails(
    val id: Int,
    val allanimeId: String,
    val name: String,
    val description: String,
    val episodesCount: Int,
    val episodes: List<Episode>,
    val genres: List<String>,
    val tags: List<Tag>,
    val season: ShowSeason,
    val year: Int,
    val format: String,
    val image: ShowImage,

    val banner: String?,
    val progress: Int?,
    val status: ShowStatus?,
)