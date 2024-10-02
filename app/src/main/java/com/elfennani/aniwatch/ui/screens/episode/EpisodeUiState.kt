package com.elfennani.aniwatch.ui.screens.episode

import androidx.compose.runtime.Stable
import androidx.media3.session.MediaController

@Stable
data class EpisodeUiState(
    val isLoading: Boolean = false,
    val errors: List<Int> = emptyList(),
    val exoPlayer: MediaController? = null,
)
