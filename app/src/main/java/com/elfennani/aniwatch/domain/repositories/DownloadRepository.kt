package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.DownloadState
import com.elfennani.aniwatch.domain.models.EpisodeAudio

interface DownloadRepository {
    suspend fun addDownload(showId: Int, episode: Double, audio: EpisodeAudio): DownloadState
    suspend fun getToBeDownloaded()
    suspend fun markDownloadState(showId: Int, episode: Double, state: DownloadState)
    suspend fun progressDownload(showId: Int, episode: Double, progress: Float)
    suspend fun setError(showId: Int, episode: Double, errorRes: Int)
    suspend fun deleteDownload(showId: Int, episode: Double)
}