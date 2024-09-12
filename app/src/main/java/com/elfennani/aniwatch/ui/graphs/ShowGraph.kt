package com.elfennani.aniwatch.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.elfennani.aniwatch.ui.screens.characters.charactersScreen
import com.elfennani.aniwatch.ui.screens.episode.episodeScreen
import com.elfennani.aniwatch.ui.screens.relations.relationScreen
import com.elfennani.aniwatch.ui.screens.search.searchScreen
import com.elfennani.aniwatch.ui.screens.show.ShowRoute
import com.elfennani.aniwatch.ui.screens.show.showScreen
import com.elfennani.aniwatch.ui.screens.status.statusEditorScreen
import kotlinx.serialization.Serializable

@Serializable
object ShowGraph

fun NavGraphBuilder.showGraph(navController: NavHostController) {
    showScreen(navController)
    statusEditorScreen(navController)
    episodeScreen(navController)
    searchScreen(navController)
    charactersScreen(navController)
    relationScreen(navController)
//    navigation<ShowGraph>(startDestination = ShowRoute) {
//        showScreen(navController)
//        statusEditorScreen(navController)
//        episodeScreen(navController)
//        searchScreen(navController)
//        charactersScreen(navController)
//        relationScreen(navController)
//    }
}