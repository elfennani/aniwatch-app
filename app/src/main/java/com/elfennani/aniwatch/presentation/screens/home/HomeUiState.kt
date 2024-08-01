package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.runtime.Stable
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.ShowBasic

@Stable
data class HomeUiState(
    val shows: List<ShowBasic> = emptyList(),
    val errors: List<Int> = emptyList(),
    val isLoading: Boolean = true,
    val isFetching: Boolean = false,
    val feed: List<Activity> = emptyList()
)