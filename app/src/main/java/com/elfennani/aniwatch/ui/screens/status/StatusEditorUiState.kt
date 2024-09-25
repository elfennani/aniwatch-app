package com.elfennani.aniwatch.ui.screens.status

import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.StatusDetails

data class StatusEditorUiState(
    val show: ShowDetails? = null,
    val status: StatusDetails? = null,
    val errors: List<Int> = emptyList(),
    val isPending: Boolean = true,
    val isEditingPending: Boolean = false,
    val dateModal: StatusDateModal? = null,
    val bottomModal: EditorSheetModal? = null
)
