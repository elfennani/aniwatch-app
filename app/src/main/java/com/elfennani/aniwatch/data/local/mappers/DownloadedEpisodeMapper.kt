package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.data.local.entities.DownloadedEpisodeEntity
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.EpisodeAudio
import java.time.Instant
import java.util.Date

fun DownloadedEpisodeEntity.toAppModel() = when (state) {
    LocalDownloadState.DONE -> DownloadState.Downloaded(audio)
    LocalDownloadState.DOWNLOADING -> DownloadState.Downloading(progress)
    LocalDownloadState.FAILURE -> DownloadState.Failure(errorRes!!)
    LocalDownloadState.PENDING -> DownloadState.Pending
}

fun DownloadState.toLocalDownloadState() = when(this){
    is DownloadState.Downloaded -> LocalDownloadState.DONE
    is DownloadState.Downloading -> LocalDownloadState.DOWNLOADING
    is DownloadState.Failure -> LocalDownloadState.FAILURE
    DownloadState.Pending -> LocalDownloadState.PENDING
    DownloadState.NotSaved -> throw Exception()
}

fun DownloadState.toEntity(
    showId: Int,
    episode: Double,
    audio: EpisodeAudio,
    createdAt: Date = Date.from(Instant.now()),
): DownloadedEpisodeEntity {
    val download = DownloadedEpisodeEntity(
        showId = showId,
        episode = episode,
        state = LocalDownloadState.PENDING,
        audio = audio,
        progress = 0f,
        createdAt = createdAt,
        errorRes = null
    )

    return when (this) {
        is DownloadState.Downloaded -> download.copy(
            state = LocalDownloadState.DONE,
            audio = audio
        )

        is DownloadState.Downloading -> download.copy(
            state = LocalDownloadState.DOWNLOADING,
            progress = progress
        )

        is DownloadState.Failure -> download.copy(
            state = LocalDownloadState.FAILURE,
            errorRes = message
        )

        DownloadState.Pending -> download.copy(
            state = LocalDownloadState.PENDING
        )

        DownloadState.NotSaved -> throw Exception()
    }
}