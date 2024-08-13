package com.elfennani.aniwatch.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.entities.asDomain
import com.elfennani.aniwatch.data.local.entities.asEntity
import com.elfennani.aniwatch.models.Download
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.ShowDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.time.Instant
import java.util.Date

class DownloadRepository(
    private val cachedShowDao: CachedShowDao,
    private val downloadDao: DownloadDao,
    private val context: Context,
) {
    private val workManager = WorkManager.getInstance(context)

    fun getDownloads(): Flow<List<Download>> {
        return downloadDao.getDownloadsFlow().map {
            it.map { download -> download.asDomain() }
        }
    }

    suspend fun clearDownloads() {
        downloadDao.deleteAll()
        workManager.cancelUniqueWork("downloadWork")
    }

    fun getDownloadedFlow() =
        downloadDao.getDownloadedFlow().map { list -> list.map { it.asDomain() } }

    suspend fun getDownloaded() = downloadDao.getDownloaded().map { it.asDomain() }

    fun startWorking() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val downloadRequest = OneTimeWorkRequest
            .Builder(DownloadWork::class.java)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "downloadWork",
            ExistingWorkPolicy.KEEP,
            downloadRequest
        )
    }

    suspend fun deleteEpisode(episode: Int, showId: Int) {
        downloadDao.deleteDownload(showId, episode)

        val directory = File(context.filesDir, "shows/$showId")
        directory.listFiles()
            ?.find { it.name.startsWith(episode.toString()) }
            ?.delete()
    }

    suspend fun downloadEpisode(show: ShowDetails, episode: Int, audio: EpisodeAudio) {
        downloadDao.insertDownload(
            Download(
                title = show.name,
                createdAt = Date(Instant.now().toEpochMilli()),
                showId = show.id,
                allanimeId = show.allanimeId,
                episode = episode,
                status = DownloadStatus.PENDING,
                audio = audio
            ).asEntity()
        )

        startWorking()
    }
}