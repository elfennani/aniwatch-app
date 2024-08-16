package com.elfennani.aniwatch.presentation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.elfennani.aniwatch.presentation.screens.episode.episodeScreen
import com.elfennani.aniwatch.presentation.screens.search.searchScreen
import com.elfennani.aniwatch.presentation.screens.show.SHOW_SCREEN_PATTERN
import com.elfennani.aniwatch.presentation.screens.show.showScreen
import com.elfennani.aniwatch.presentation.screens.status.statusEditorScreen

const val ShowGraphPattern = "show"
fun NavGraphBuilder.showGraph(navController: NavHostController){
    navigation(
        startDestination = SHOW_SCREEN_PATTERN,
        route = ShowGraphPattern
    ){
        showScreen(navController)
        statusEditorScreen(navController)
        episodeScreen(navController)
        searchScreen(navController)
    }
}