package com.elfennani.aniwatch.data_old.repository

import com.elfennani.aniwatch.data_old.local.dao.DownloadDao
import com.elfennani.aniwatch.data_old.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data_old.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.data_old.local.entities.toAppModel
import com.elfennani.aniwatch.data_old.local.entities.toLocalDownloadState
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.Date

class DownloadRepository(
    private val downloadDao: DownloadDao,
) {
    suspend fun addDownload(showId: Int, episode: Double, audio: EpisodeAudio): com.elfennani.aniwatch.domain.models.DownloadState {
        val download = LocalDownloadedEpisode(
            showId = showId,
            episode = episode,
            state = LocalDownloadState.PENDING,
            audio = audio,
            progress = 0.0f,
            createdAt = Date.from(Instant.now()),
            errorRes = null
        )
        downloadDao.upsertDownload(download)

        return download.toAppModel()
    }

    suspend fun getToBeDownloaded(): List<LocalDownloadedEpisode> {
        return downloadDao.getDownloads().first()
    }

    suspend fun markDownloadState(showId: Int, episode: Double, state: com.elfennani.aniwatch.domain.models.DownloadState) {
        if (state is com.elfennani.aniwatch.domain.models.DownloadState.Downloading) {
            progressDownload(showId, episode, state.progress)
            return;
        }
        if (state is com.elfennani.aniwatch.domain.models.DownloadState.Failure) {
            setError(showId, episode, state.message)
            return
        }
        downloadDao.updateState(
            showId,
            episode,
            state.toLocalDownloadState()
        )
    }

    suspend fun progressDownload(showId: Int, episode: Double, progress: Float) {
        downloadDao.updateProgress(showId, episode, progress)
    }

    suspend fun setError(showId: Int, episode: Double, errorRes: Int) {
        downloadDao.updateError(showId, episode, errorRes)
    }

    suspend fun deleteDownload(showId: Int, episode: Double) {
        downloadDao.deleteDownloadByShowId(showId, episode)
    }
}