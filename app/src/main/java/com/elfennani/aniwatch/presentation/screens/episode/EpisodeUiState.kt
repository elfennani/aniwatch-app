package com.elfennani.aniwatch.presentation.screens.episode

import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.ShowDetails

data class EpisodeUiState(
    val episode: EpisodeLink? = null,
    val show: ShowDetails? = null,
    val episodeDetails: Episode? = null,
    val loading: Boolean = false,
    val error: String? = null
)
