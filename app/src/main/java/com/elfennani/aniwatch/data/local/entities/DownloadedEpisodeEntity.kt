package com.elfennani.aniwatch.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.elfennani.aniwatch.models.EpisodeAudio
import java.time.Instant
import java.util.Date

enum class LocalDownloadState {
    DONE, DOWNLOADING, FAILURE, PENDING
}

@Entity(
    tableName = "downloaded_episodes",
    primaryKeys = ["showId", "episode"]
)
data class DownloadedEpisodeEntity(
    val showId: Int,
    val episode: Double,
    val state: LocalDownloadState,
    @ColumnInfo(defaultValue = "SUB") val audio: EpisodeAudio,
    val progress: Float,
    val createdAt: Date,
    val errorRes: Int?,
)