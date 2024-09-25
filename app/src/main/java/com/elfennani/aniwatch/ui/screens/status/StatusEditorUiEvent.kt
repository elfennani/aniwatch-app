package com.elfennani.aniwatch.ui.screens.status

import com.elfennani.aniwatch.domain.models.ShowStatus

sealed class StatusEditorUiEvent {
    class OpenDateModal(val modal: StatusDateModal) : StatusEditorUiEvent()
    class SetModalDate(val dateModal: StatusDateModal, val dateMillis: Long?) :
        StatusEditorUiEvent()

    data object CloseModal : StatusEditorUiEvent()

    class OpenBottomSheet(val modal: EditorSheetModal) : StatusEditorUiEvent()
    data object CloseBottomSheet : StatusEditorUiEvent()

    data class SetScore(val score: Int) : StatusEditorUiEvent()
    data class SetProgress(val progress: Int) : StatusEditorUiEvent()
    data class SetStatus(val status: ShowStatus): StatusEditorUiEvent()

    data class Save(val onSuccess: () -> Unit): StatusEditorUiEvent()
}