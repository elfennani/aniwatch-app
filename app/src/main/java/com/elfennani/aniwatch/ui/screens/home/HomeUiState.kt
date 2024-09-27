package com.elfennani.aniwatch.ui.screens.home

import androidx.compose.runtime.Stable
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.User

@Stable
data class HomeUiState(
    val shows: List<Show> = emptyList(),
    val user: User? = null,
    val errors: List<Int> = emptyList(),
    val isLoading: Boolean = true,
    val isFetching: Boolean = false,
)