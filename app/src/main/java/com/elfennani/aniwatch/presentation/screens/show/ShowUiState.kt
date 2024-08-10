package com.elfennani.aniwatch.presentation.screens.show

import com.elfennani.aniwatch.models.Download
import com.elfennani.aniwatch.models.ShowDetails

data class ShowUiState(
    val show: ShowDetails? = null,
    val savedEpisodes: List<Download> = emptyList(),
    val isLoading: Boolean = true,
    val errors: List<Int> = emptyList()
)
