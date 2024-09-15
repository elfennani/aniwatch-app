package com.elfennani.aniwatch.ui.screens.show

import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.ShowDetails

data class ShowUiState(
    val show: ShowDetails? = null,
    val isLoading: Boolean = true,
    val errors: List<Int> = emptyList(),
    val defaultAudio: EpisodeAudio? = null,
    val isAppendingEpisode: Boolean = false
)
