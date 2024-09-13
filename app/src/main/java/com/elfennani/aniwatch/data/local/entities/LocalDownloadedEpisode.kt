package com.elfennani.aniwatch.data.local.entities

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.services.DownloadService
import java.time.Instant
import java.util.Date

@Entity(
    tableName = "downloaded_episodes",
    primaryKeys = ["showId", "episode"]
)
data class LocalDownloadedEpisode(
    val showId: Int,
    val episode: Double,
    val state: LocalDownloadState,
    @ColumnInfo(defaultValue = "SUB") val audio: EpisodeAudio,
    val progress: Float,
    val createdAt: Date,
    val errorRes: Int?,
)

fun LocalDownloadedEpisode.toAppModel() = when (state) {
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
): LocalDownloadedEpisode {
    val download = LocalDownloadedEpisode(
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