package com.elfennani.aniwatch.presentation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.elfennani.aniwatch.presentation.screens.episode.episodeScreen
import com.elfennani.aniwatch.presentation.screens.show.SHOW_SCREEN_PATTERN
import com.elfennani.aniwatch.presentation.screens.show.showScreen

const val ShowGraphPattern = "show"
fun NavGraphBuilder.showGraph(navController: NavHostController){
    navigation(
        startDestination = SHOW_SCREEN_PATTERN,
        route = ShowGraphPattern
    ){
        showScreen(navController)
        episodeScreen(navController)
    }
}