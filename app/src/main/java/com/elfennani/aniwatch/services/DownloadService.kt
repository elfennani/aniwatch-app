package com.elfennani.aniwatch.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log
import androidx.compose.ui.util.fastRoundToInt
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.os.bundleOf
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data_old.remote.APIService
import com.elfennani.aniwatch.data_old.repository.DownloadRepository
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.ResourceException
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.dataOrThrow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL
import java.time.Duration
import java.time.Instant
import javax.inject.Inject


@AndroidEntryPoint
class DownloadService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null


    @Inject
    lateinit var showRepository: ShowRepository

    @Inject
    lateinit var apiService: APIService

    @Inject
    lateinit var downloadRepository: DownloadRepository


    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var lastNotificationUpdate: Long = 0

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            val showId = msg.data.getInt("showId")
            val episodeNumber = msg.data.getDouble("episode")
            val audio = EpisodeAudio.valueOf(msg.data.getString("audio")!!)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat
                .Builder(this@DownloadService, "DOWNLOADS")
                .setContentTitle("#$showId")
                .setContentText("Downloading Episode $episodeNumber • $audio")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setSilent(true)

            Log.d(
                "DownloadService",
                "handleMessage: downloading episode $episodeNumber in $audio of show $showId"
            )

            try {
                runBlocking {
                    downloadRepository.markDownloadState(
                        showId,
                        episodeNumber,
                        _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Downloading(0f)
                    )
                    notificationManager.notify(100, notification.build())

                    Log.d("DownloadService", "handleMessage: Loading Show")
                    val show = showRepository
                        .getShowById(showId)
                        .dataOrThrow(this@DownloadService)

                    Log.d("DownloadService", "handleMessage: Loading Episode")
                    val episode = showRepository
                        .getEpisodeById(show.allanimeId, episodeNumber, audio)
                        .dataOrThrow(this@DownloadService)

                    notificationManager.notify(
                        100,
                        notification
                            .setContentTitle(show.name)
                            .build()
                    )

                    val updateProgress: (suspend (Float) -> Unit) = { progress ->
                        downloadRepository.progressDownload(showId, episodeNumber, progress)
                        updateNotification(show, episodeNumber, audio, progress)
                    }

                    updateProgress(0f)
                    Log.d("DownloadService", "handleMessage: Downloading Episode File")
                    val directory = File(applicationContext.filesDir, "shows/$showId")
                    if (!directory.exists()) directory.mkdirs()
                    val file = File(directory, "$episodeNumber.mp4")

                    if (episode.mp4 == null) {
                        Log.d("DownloadService", "handleMessage: empty episode")
                    }

                    val url = URL(episode.mp4!!)
                    val connection = url.openConnection()
                    connection.connect()

                    var count: Int
                    val lengthOfFile = connection.contentLength
                    val input = connection.getInputStream()
                    val output = file.outputStream()
                    val data = ByteArray(1024)
                    var total = 0L

                    while ((input.read(data).also { count = it }) != -1) {
                        total += count.toLong()
                        output.write(data, 0, count)

                        val lastDifference =Instant.now().toEpochMilli() - lastNotificationUpdate
                        val timeout = Duration.ofSeconds(1).toMillis()
                        if (lastDifference < timeout) {
                            continue;
                        }
                        lastNotificationUpdate = Instant.now().toEpochMilli()
                        updateProgress(total.toFloat() / lengthOfFile)
                        Log.d("DownloadService", "handleMessage: $lengthOfFile $total")
                    }

                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    val doneNotification = NotificationCompat
                        .Builder(this@DownloadService, "DOWNLOADS")
                        .setContentTitle(show.name)
                        .setContentText("Episode $episodeNumber Downloaded")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build()

                    notificationManager.notify(showId, doneNotification)
                    downloadRepository.markDownloadState(
                        showId,
                        episodeNumber,
                        _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Downloaded(audio)
                    )
                }
            } catch (e: InterruptedException) {
                Log.d("DownloadService", "handleMessage: Interruption $e")
                Thread.currentThread().interrupt()
            } catch (e: ResourceException) {
                Log.d(
                    "DownloadService",
                    "handleMessage: ResourceException ${getString(e.errorResource)}"
                )
                runBlocking {
                    downloadRepository.markDownloadState(
                        showId,
                        episodeNumber,
                        _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Failure(e.errorResource)
                    )
                }
            } catch (e: Exception) {
                Log.d("DownloadService", "handleMessage: EXCEPTION: $e")
                e.printStackTrace()
                runBlocking {
                    downloadRepository.markDownloadState(
                        showId,
                        episodeNumber,
                        _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Failure(R.string.something_wrong)
                    )
                }
            }

            stopSelf(msg.arg1)
        }
    }

    private fun updateNotification(
        show: ShowDetails,
        episode: Double,
        audio: EpisodeAudio,
        progress: Float,
    ) {


        Log.d("DownloadService", "updateNotification: progress: $progress")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat
            .Builder(this@DownloadService, "DOWNLOADS")
            .setContentTitle(show.name)
            .setContentText("Downloading Episode $episode • $audio")
            .setProgress(100, (progress*100).fastRoundToInt(), false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSilent(true)
            .setOngoing(true)
            .build()

        notificationManager.notify(100, notification)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground()

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            val showId = intent.getIntExtra("showId", 0)
            val episode = intent.getDoubleExtra("episode", 0.0)
            val audio = intent.getStringExtra("audio")!!

            msg.data = bundleOf(
                "showId" to showId,
                "episode" to episode,
                "audio" to audio,
            )
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, "DOWNLOADS")
            .setContentTitle("Download Pending")
            .setContentText("Loading...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSilent(true)
            .setOngoing(true)
            .build()

        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ 100,
            /* notification = */ notification,
            /* foregroundServiceType = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            else 0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}