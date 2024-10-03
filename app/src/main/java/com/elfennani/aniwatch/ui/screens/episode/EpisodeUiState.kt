package com.elfennani.aniwatch.ui.screens.episode

import androidx.compose.runtime.Stable
import androidx.media3.session.MediaController
import com.elfennani.aniwatch.domain.models.Show

@Stable
data class EpisodeUiState(
    val isLoading: Boolean = true,
    val errors: List<Int> = emptyList(),
    val exoPlayer: MediaController? = null,
    val show: Show? = null
)
