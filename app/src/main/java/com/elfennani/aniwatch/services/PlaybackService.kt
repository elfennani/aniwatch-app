package com.elfennani.aniwatch.services

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this)
            .build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        stopSelf()
        super.onDestroy()
    }

    override fun onGetSession(p0: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            // Stop the foreground service and allow it to be stopped
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }
}