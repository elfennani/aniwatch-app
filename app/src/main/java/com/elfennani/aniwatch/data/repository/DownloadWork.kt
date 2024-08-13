package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.logging.Handler

@HiltWorker
class DownloadWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val showRepository: ShowRepository,
    private val apiService: APIService,
    private val downloadDao: DownloadDao,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        android.os.Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                /* context = */ applicationContext,
                /* text = */ "Starting Download...",
                /* duration = */ Toast.LENGTH_SHORT
            ).show()
        }
        return withContext(Dispatchers.IO) {
            try {
                performWork()
                return@withContext Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.failure()
            }
        }
    }

    private suspend fun performWork() {
        val pendingDownload = downloadDao.getLatestDownload()

        if (pendingDownload != null) {
            when (val show = showRepository.getShowById(pendingDownload.showId)) {
                is Resource.Success -> {
                    val isSuccess = downloadEpisode(
                        showId = show.data!!.id,
                        allanimeId = show.data.allanimeId,
                        episode = pendingDownload.episode,
                        audio = EpisodeAudio.valueOf(pendingDownload.audio)
                    )

                    if(isSuccess)
                        downloadDao.updateDownload(pendingDownload.copy(status = DownloadStatus.COMPLETED.name))
                    else
                        downloadDao.deleteDownload(pendingDownload)

                    performWork()
                }

                is Resource.Error -> throw Exception(applicationContext.resources.getString(show.message!!))
            }
        }
    }

    private suspend fun downloadEpisode(showId: Int, allanimeId: String, episode: Int, audio: EpisodeAudio): Boolean {
        return when (val data = showRepository.getEpisodeById(allanimeId, episode, audio)) {
            is Resource.Success -> {
                val directory = File(applicationContext.filesDir, "shows/$showId")
                if (!directory.exists()) directory.mkdirs()
                val file = File(directory, "$episode-${audio.name}.mp4")
                val outputStream = file.outputStream()

                if(data.data?.mp4 == null) return false

                apiService.downloadFile(data.data.mp4).body()?.byteStream().use {
                    Log.d("DownloadWork", "downloadEpisode: ${it?.available()}")
                    it?.copyTo(outputStream)
                }
                withContext(Dispatchers.IO) { outputStream.close() }

                true
            }

            is Resource.Error -> throw Exception(applicationContext.resources.getString(data.message!!))
        }
    }
}