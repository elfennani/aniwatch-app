package com.elfennani.aniwatch.domain.models

data class EpisodeLink(
    val mp4: String?,
    val hls: StreamLink?,
    val dubbed: Boolean
)
