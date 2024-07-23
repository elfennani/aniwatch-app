package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.runtime.Stable
import com.elfennani.aniwatch.models.ShowBasic

@Stable
data class HomeUiState(
    val shows: List<ShowBasic> = emptyList(),
    val error: String? = null,
    val isFetching: Boolean = false
) {
//    data object Loading : HomeUiState()
//    data class Data(val shows: List<ShowBasic>) : HomeUiState()
//    data class Error(val error: String) : HomeUiState()
//    data class FetchingError(val shows: List<ShowBasic>, val error: String) : HomeUiState()
//    data class Refetching(val shows: List<ShowBasic>): HomeUiState()
}