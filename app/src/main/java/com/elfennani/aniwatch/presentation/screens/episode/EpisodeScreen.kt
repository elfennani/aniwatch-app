package com.elfennani.aniwatch.presentation.screens.episode

import android.text.TextUtils.replace
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.presentation.theme.AppTheme

@Composable
fun EpisodeScreen(
    navController: NavController,
    state: EpisodeUiState
) {
    Scaffold(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground
    ) {
        Column(Modifier.padding(it)) {
            Text(text = "This is episode Screen")
            if(state.episode != null){
                Text(text = state.episode.toString())
            }

            if(state.error != null){
                Text(text = state.error)
            }
        }
    }
}

const val EPISODE_SCREEN_PATTERN = "episode/{id}/{allanimeId}/{episode}"
fun NavGraphBuilder.episodeScreen(navController: NavController) {
    composable(
        route = EPISODE_SCREEN_PATTERN,
        arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("allanimeId") { type = NavType.StringType },
            navArgument("episode") { type = NavType.IntType }
        ),
    ) {
        val viewModel: EpisodeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        EpisodeScreen(
            navController = navController,
            state = state
        )
    }
}

fun NavController.navigateToEpisodeScreen(
    id: Int,
    allanimeId: String,
    episode: Int,
    popUpToTop: Boolean = false
) {
    this.navigate(
        EPISODE_SCREEN_PATTERN
            .replace("{id}", id.toString())
            .replace("{allanimeId}", allanimeId)
            .replace("{episode}", episode.toString())
    ) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}