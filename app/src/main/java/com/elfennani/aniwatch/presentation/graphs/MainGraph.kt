package com.elfennani.aniwatch.presentation.graphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.aniwatch.presentation.composables.MainScaffold
import com.elfennani.aniwatch.presentation.screens.home.HomeScreenPattern
import com.elfennani.aniwatch.presentation.screens.home.homeScreen
import com.elfennani.aniwatch.presentation.screens.listings.listingScreen
import com.elfennani.aniwatch.presentation.screens.settings.settingsScreen
import com.elfennani.aniwatch.presentation.screens.show.SHOW_SCREEN_PATTERN

const val TRANSITION_DURATION = 250
const val MainGraphPattern = "home"
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(
        route = MainGraphPattern,
        popEnterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
        exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
    ) {
        val bottomBarNavController = rememberNavController()
        MainScaffold(navController = bottomBarNavController) {
            NavHost(
                navController = bottomBarNavController,
                startDestination = HomeScreenPattern,
                route = MainGraphPattern,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                homeScreen(navController,it)
                listingScreen(navController,it)
                settingsScreen(navController,it)
            }
        }

    }
}

fun NavController.navigateToMainGraph(popUpToTop: Boolean = false) {
    navigate(MainGraphPattern) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}

