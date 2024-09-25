package com.elfennani.aniwatch.ui.screens.home

import androidx.compose.runtime.Stable
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.User

@Stable
data class HomeUiState(
    val shows: List<ShowBasic> = emptyList(),
    val user: User? = null,
    val errors: List<Int> = emptyList(),
    val isLoading: Boolean = true,
    val isFetching: Boolean = false,
    val feed: List<Activity> = emptyList()
)