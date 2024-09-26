package com.elfennani.aniwatch.ui.screens.listings

import com.elfennani.aniwatch.domain.models.enums.ShowStatus

data class ListingUiState (
    val status: ShowStatus = ShowStatus.COMPLETED,
    val listing: List<ShowBasic> = emptyList(),
    val errors: List<Int> = emptyList(),
    val isLoading: Boolean = true
)