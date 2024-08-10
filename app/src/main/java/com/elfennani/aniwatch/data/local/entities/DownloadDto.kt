package com.elfennani.aniwatch.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.models.Download
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowSeason
import java.util.Date

@Entity(tableName = "downloads", primaryKeys = ["showId", "episode"])
data class DownloadDto(
    val title: String,
    val createdAt: Date,
    val showId: Int,
    val allanimeId: String,
    val episode: Int,
    val status: String,
    @ColumnInfo(defaultValue = "SUB") val audio: String
)

fun Download.asEntity() = DownloadDto(
    allanimeId = allanimeId,
    showId = showId,
    title = title,
    status = status.name,
    episode = episode,
    createdAt = createdAt,
    audio = audio.name
)

fun DownloadDto.asDomain() = Download(
    title = title,
    createdAt = createdAt,
    showId = showId,
    allanimeId = allanimeId,
    episode = episode,
    status = DownloadStatus.valueOf(status),
    audio = EpisodeAudio.valueOf(audio)
)
