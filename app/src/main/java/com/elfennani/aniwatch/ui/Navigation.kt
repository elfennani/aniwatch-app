package com.elfennani.aniwatch.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.ui.graphs.AuthGraphPattern
import com.elfennani.aniwatch.ui.graphs.MainGraphPattern
import com.elfennani.aniwatch.ui.graphs.authGraph
import com.elfennani.aniwatch.ui.graphs.mainGraph
import com.elfennani.aniwatch.ui.graphs.showGraph
import com.elfennani.aniwatch.sessionId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

const val ANIM_DURATION_MILLIS = 100

@Composable
fun Navigation(
    navController: NavHostController
) {
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