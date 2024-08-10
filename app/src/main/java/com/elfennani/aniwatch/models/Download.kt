package com.elfennani.aniwatch.models

import java.util.Date

data class Download(
    val title: String,
    val createdAt: Date,
    val showId: Int,
    val allanimeId: String,
    val episode: Int,
    val status: DownloadStatus,
    val audio: EpisodeAudio
)
