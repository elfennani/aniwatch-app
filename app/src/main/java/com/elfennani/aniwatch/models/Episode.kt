package com.elfennani.aniwatch.models

data class Episode(
    val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Int,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
    val state: DownloadState
)