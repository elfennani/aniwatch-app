package com.elfennani.aniwatch.domain.models

data class Episode(
    val episode: Double,
    val title: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
    val id: String,
)