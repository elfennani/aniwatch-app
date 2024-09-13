package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode

@Entity("cached_episodes")
data class CachedEpisodeDto(
    @PrimaryKey val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Double,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
)

fun CachedEpisodeDto.toDomain(downloadedEpisode: LocalDownloadedEpisode?) = Episode(
    id = id,
    allanimeId = allanimeId,
    animeId = animeId,
    episode = episode,
    name = name,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration,
    state = when (downloadedEpisode?.state) {
        LocalDownloadState.DOWNLOADING -> DownloadState.Downloading(downloadedEpisode.progress)
        LocalDownloadState.DONE -> DownloadState.Downloaded(downloadedEpisode.audio)
        LocalDownloadState.FAILURE -> DownloadState.Failure(
            downloadedEpisode.errorRes ?: R.string.something_wrong
        )
        LocalDownloadState.PENDING -> DownloadState.Pending
        else -> DownloadState.NotSaved
    }
)

fun Episode.toCached() = CachedEpisodeDto(
    id = id,
    allanimeId = allanimeId,
    animeId = animeId,
    episode = episode,
    name = name,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration
)