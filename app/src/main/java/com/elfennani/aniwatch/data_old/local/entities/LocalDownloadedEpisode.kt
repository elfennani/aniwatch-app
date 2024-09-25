package com.elfennani.aniwatch.data_old.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.elfennani.aniwatch.domain.models.EpisodeAudio
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
    LocalDownloadState.DONE -> com.elfennani.aniwatch.domain.models.DownloadState.Downloaded(audio)
    LocalDownloadState.DOWNLOADING -> com.elfennani.aniwatch.domain.models.DownloadState.Downloading(progress)
    LocalDownloadState.FAILURE -> com.elfennani.aniwatch.domain.models.DownloadState.Failure(errorRes!!)
    LocalDownloadState.PENDING -> com.elfennani.aniwatch.domain.models.DownloadState.Pending
}

fun com.elfennani.aniwatch.domain.models.DownloadState.toLocalDownloadState() = when(this){
    is com.elfennani.aniwatch.domain.models.DownloadState.Downloaded -> LocalDownloadState.DONE
    is com.elfennani.aniwatch.domain.models.DownloadState.Downloading -> LocalDownloadState.DOWNLOADING
    is com.elfennani.aniwatch.domain.models.DownloadState.Failure -> LocalDownloadState.FAILURE
    com.elfennani.aniwatch.domain.models.DownloadState.Pending -> LocalDownloadState.PENDING
    com.elfennani.aniwatch.domain.models.DownloadState.NotSaved -> throw Exception()
}

fun com.elfennani.aniwatch.domain.models.DownloadState.toEntity(
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
        is com.elfennani.aniwatch.domain.models.DownloadState.Downloaded -> download.copy(
            state = LocalDownloadState.DONE,
            audio = audio
        )

        is com.elfennani.aniwatch.domain.models.DownloadState.Downloading -> download.copy(
            state = LocalDownloadState.DOWNLOADING,
            progress = progress
        )

        is com.elfennani.aniwatch.domain.models.DownloadState.Failure -> download.copy(
            state = LocalDownloadState.FAILURE,
            errorRes = message
        )

        com.elfennani.aniwatch.domain.models.DownloadState.Pending -> download.copy(
            state = LocalDownloadState.PENDING
        )

        com.elfennani.aniwatch.domain.models.DownloadState.NotSaved -> throw Exception()
    }
}