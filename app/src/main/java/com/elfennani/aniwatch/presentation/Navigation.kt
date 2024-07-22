package com.elfennani.aniwatch.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.presentation.graphs.AuthGraphPattern
import com.elfennani.aniwatch.presentation.graphs.MainGraphPattern
import com.elfennani.aniwatch.presentation.graphs.authGraph
import com.elfennani.aniwatch.presentation.graphs.mainGraph
import com.elfennani.aniwatch.sessionId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val session = runBlocking { context.dataStore.data.first().sessionId }
    val startingDestination = if (session != null) MainGraphPattern else AuthGraphPattern

    NavHost(
        navController = navController,
        startDestination = startingDestination,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}