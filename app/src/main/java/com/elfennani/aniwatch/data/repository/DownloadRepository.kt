package com.elfennani.aniwatch.data.repository

import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.data.local.entities.toAppModel
import com.elfennani.aniwatch.data.local.entities.toEntity
import com.elfennani.aniwatch.data.local.entities.toLocalDownloadState
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.EpisodeAudio
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.Date

class DownloadRepository(
    private val downloadDao: DownloadDao,
) {
    suspend fun addDownload(showId: Int, episode: Int, audio: EpisodeAudio): DownloadState {
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

    suspend fun markDownloadState(showId: Int, episode: Int, state: DownloadState) {
        if (state is DownloadState.Downloading) {
            progressDownload(showId, episode, state.progress)
            return;
        }
        if (state is DownloadState.Failure) {
            setError(showId, episode, state.message)
            return
        }
        downloadDao.updateState(
            showId,
            episode,
            state.toLocalDownloadState()
        )
    }

    suspend fun progressDownload(showId: Int, episode: Int, progress: Float) {
        downloadDao.updateProgress(showId, episode, progress)
    }

    suspend fun setError(showId: Int, episode: Int, errorRes: Int) {
        downloadDao.updateError(showId, episode, errorRes)
    }

    suspend fun deleteDownload(showId: Int, episode: Int) {
        downloadDao.deleteDownloadByShowId(showId, episode)
    }
}