package com.elfennani.aniwatch.ui.screens.episode

import androidx.compose.runtime.Stable
import androidx.media3.common.Tracks
import androidx.media3.session.MediaController
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.ShowDetails

@Stable
data class EpisodeUiState(
        val loading: Boolean = false,
    val errors: List<Int> = emptyList(),
    val exoPlayer: MediaController? = null,
)
