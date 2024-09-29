package com.elfennani.aniwatch.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.elfennani.aniwatch.ui.screens.downloads.downloadsScreen
import com.elfennani.aniwatch.ui.screens.home.HomeScreenPattern
import com.elfennani.aniwatch.ui.screens.home.homeScreen
import com.elfennani.aniwatch.ui.screens.listings.listingScreen

const val TRANSITION_DURATION = 250
const val MainGraphPattern = "home"
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = HomeScreenPattern,
        route = MainGraphPattern
    ){
        homeScreen(navController)
        listingScreen(navController)
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

