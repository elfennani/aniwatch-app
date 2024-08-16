package com.elfennani.aniwatch.presentation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.elfennani.aniwatch.presentation.screens.home.homeScreen
import com.elfennani.aniwatch.presentation.screens.listings.listingScreen
import com.elfennani.aniwatch.presentation.screens.downloads.downloadsScreen
import com.elfennani.aniwatch.presentation.screens.home.HomeScreenPattern
import com.elfennani.aniwatch.presentation.screens.settings.settingsScreen
import com.elfennani.aniwatch.presentation.screens.show.SHOW_SCREEN_PATTERN

const val TRANSITION_DURATION = 250
const val MainGraphPattern = "home"
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = HomeScreenPattern,
        route = MainGraphPattern
    ){
        homeScreen(navController)
        listingScreen(navController)
        settingsScreen(navController)
        downloadsScreen(navController)
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

