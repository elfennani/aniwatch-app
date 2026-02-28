package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.entities.EpisodeEntity
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data.local.entities.DownloadedEpisodeEntity
import com.elfennani.aniwatch.data.local.entities.LocalEpisodeEntity
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode

fun EpisodeEntity.toDomain(downloadedEpisode: DownloadedEpisodeEntity?, localEpisodeEntity: LocalEpisodeEntity? = null) = Episode(
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
    },
    uri = localEpisodeEntity?.uri
)

fun Episode.toCached() = EpisodeEntity(
    id = id,
    allanimeId = allanimeId,
    animeId = animeId,
    episode = episode,
    name = name,
    dubbed = dubbed,
    thumbnail = thumbnail,
    duration = duration
)