package com.elfennani.aniwatch.presentation.screens.show

import com.elfennani.aniwatch.models.ShowDetails

data class ShowUiState(
    val show: ShowDetails? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
