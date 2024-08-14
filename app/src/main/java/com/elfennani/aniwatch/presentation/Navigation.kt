package com.elfennani.aniwatch.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.presentation.composables.MainScaffold
import com.elfennani.aniwatch.presentation.graphs.AuthGraphPattern
import com.elfennani.aniwatch.presentation.graphs.MainGraphPattern
import com.elfennani.aniwatch.presentation.graphs.authGraph
import com.elfennani.aniwatch.presentation.graphs.mainGraph
import com.elfennani.aniwatch.presentation.graphs.showGraph
import com.elfennani.aniwatch.presentation.theme.AppTheme
import com.elfennani.aniwatch.sessionId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

const val ANIM_DURATION_MILLIS = 100

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val session = runBlocking { context.dataStore.data.first().sessionId }
    val startingDestination = if (session != null) MainGraphPattern else AuthGraphPattern
    val spec = remember{ tween<Float>(ANIM_DURATION_MILLIS, easing = FastOutSlowInEasing) }
    val specInt = remember { tween<IntOffset>(ANIM_DURATION_MILLIS, easing = FastOutSlowInEasing) }

    NavHost(
        navController = navController,
        startDestination = startingDestination,
        enterTransition = {
            fadeIn() + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(ANIM_DURATION_MILLIS),
            ) + scaleIn(initialScale = 0.9f)
        },
        exitTransition = {
            fadeOut(animationSpec = tween(ANIM_DURATION_MILLIS)) + scaleOut(
                targetScale = 0.75f
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = spec) +
                    scaleIn(
                        initialScale = 0.9f,
                        animationSpec = spec
                    ) +
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        initialOffset = { (it * 0.15f).roundToInt() },
                        animationSpec = specInt
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = spec) +
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        targetOffset = { (it * 0.15f).roundToInt() },
                        animationSpec = specInt
                    ) +
                    scaleOut(
                        targetScale = 0.85f,
                        animationSpec = spec
                    )
        },
    ) {
        authGraph(navController)
        mainGraph(navController)
        showGraph(navController)
    }
}