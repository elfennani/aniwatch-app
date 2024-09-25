package com.elfennani.aniwatch.domain.models

data class Episode(
    val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Double,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
    val state: _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState
)