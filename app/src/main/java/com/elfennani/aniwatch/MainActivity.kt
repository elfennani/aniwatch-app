package com.elfennani.aniwatch

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.NotificationUtil.IMPORTANCE_DEFAULT
import androidx.media3.common.util.NotificationUtil.Importance
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.ui.Navigation
import com.elfennani.aniwatch.ui.composables.MainNavigation
import com.elfennani.aniwatch.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerChannels()

        setContent {
            AppTheme {
                val navController = rememberNavController()

                Scaffold(
                    containerColor = AppTheme.colorScheme.background,
                    contentColor = AppTheme.colorScheme.onBackground,
                    bottomBar = {
                        MainNavigation(navController = navController)
                    }
                ) {
                    Navigation(navController)
                }
            }
        }
    }

    private fun registerChannels() {
        val channel = NotificationChannel(
            /* id = */ "DOWNLOADS",
            /* name = */ "Downloads",
            /* importance = */ NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}