package com.elfennani.aniwatch.presentation.screens.status

import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.StatusDetails

data class StatusEditorUiState(
    val show: ShowDetails? = null,
    val status: StatusDetails? = null,
    val errors: List<Int> = emptyList(),
    val isPending: Boolean = true,
    val isEditingPending: Boolean = false
)
