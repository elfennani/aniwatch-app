package com.elfennani.aniwatch.domain.models

data class EpisodeDetails(
    val showId: Int,
    val audio: EpisodeAudio,
    val uri: String,
    val isStreaming: Boolean,
    val isLocal: Boolean
)
