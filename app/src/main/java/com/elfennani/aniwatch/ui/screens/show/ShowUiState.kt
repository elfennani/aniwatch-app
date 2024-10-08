package com.elfennani.aniwatch.ui.screens.show

import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.Show

data class ShowUiState(
    val show: Show? = null,
    val isLoading: Boolean = true,
    val errors: List<Int> = emptyList(),
    val defaultAudio: EpisodeAudio? = null,
    val isIncrementingEpisode: Boolean = false,
    val episodes: List<Episode> = emptyList()
)
