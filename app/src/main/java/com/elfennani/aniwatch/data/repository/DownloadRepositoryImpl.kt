package com.elfennani.aniwatch.data.repository

import com.elfennani.aniwatch.data_old.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.domain.models.DownloadState
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.repositories.DownloadRepository

class DownloadRepositoryImpl: DownloadRepository {
    override suspend fun addDownload(
        showId: Int,
        episode: Double,
        audio: EpisodeAudio,
    ): DownloadState {
        TODO("Not yet implemented")
    }

    override suspend fun getToBeDownloaded(): List<LocalDownloadedEpisode> {
        TODO("Not yet implemented")
    }

    override suspend fun markDownloadState(showId: Int, episode: Double, state: DownloadState) {
        TODO("Not yet implemented")
    }

    override suspend fun progressDownload(showId: Int, episode: Double, progress: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun setError(showId: Int, episode: Double, errorRes: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDownload(showId: Int, episode: Double) {
        TODO("Not yet implemented")
    }
}