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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.media3.common.util.NotificationUtil.IMPORTANCE_DEFAULT
import androidx.media3.common.util.NotificationUtil.Importance
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import com.elfennani.aniwatch.ui.Navigation
import com.elfennani.aniwatch.ui.composables.MainNavigation
import com.elfennani.aniwatch.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerChannels()

        val session = runBlocking { dataStore.data.first()[SessionRepository.SESSION_KEY] }

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
                    Navigation(navController, session)
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