package com.elfennani.aniwatch.ui.screens.episode

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeRoute(
    val showId: Int,
    val episode: Float,
    val audio: EpisodeAudio,
)

@androidx.annotation.OptIn(UnstableApi::class)
fun NavGraphBuilder.episodeScreen(navController: NavController) {
    composable<EpisodeRoute> {
        val viewModel: EpisodeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        EpisodeScreen(
            state = state,
            onRefresh = viewModel::refresh,
            onErrorDismiss = viewModel::dismissError
        )
    }
}