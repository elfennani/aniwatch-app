package com.elfennani.aniwatch.ui.screens.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.handleError
import com.elfennani.aniwatch.domain.models.toStatusDate
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.domain.usecases.FetchShowUseCase
import com.elfennani.aniwatch.domain.usecases.UpdateShowStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val showRepository: ShowRepository,
    private val fetchShowUseCase: FetchShowUseCase,
    private val updateShowStatusUseCase: UpdateShowStatusUseCase,
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!
    private val _state = MutableStateFlow(StatusEditorUiState())
    private val show = showRepository.showById(showId)

    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(isPending = true) }
        viewModelScope.launch {
            fetchShowUseCase(showId).handleError(::handleError)
        }.invokeOnCompletion { _state.update { it.copy(isPending = false) } }

        viewModelScope.launch {
            show.collectLatest { show ->
                val initialStatusDetails = StatusDetails(
                    status = show.status,
                    score = show.score ?: 0.0,
                    progress = show.progress ?: 0,
                    favorite = show.favorite,
                    startedAt = show.startedAt,
                    completedAt = show.endedAt
                )
                _state.update { it.copy(show = show, status = initialStatusDetails) }
            }
        }
    }

    fun onEvent(uiEvent: StatusEditorUiEvent) {
        when (uiEvent) {
            is StatusEditorUiEvent.OpenDateModal -> _state.update { it.copy(dateModal = uiEvent.modal) }
            is StatusEditorUiEvent.CloseModal -> _state.update { it.copy(dateModal = null) }
            is StatusEditorUiEvent.SetModalDate -> _state.update {
                val newStatus = if (uiEvent.dateModal == StatusDateModal.START) {
                    it.status?.copy(startedAt = uiEvent.dateMillis?.toStatusDate())
                } else {
                    it.status?.copy(completedAt = uiEvent.dateMillis?.toStatusDate())
                }

                it.copy(dateModal = null, status = newStatus)
            }

            is StatusEditorUiEvent.OpenBottomSheet -> _state.update { it.copy(bottomModal = uiEvent.modal) }
            is StatusEditorUiEvent.CloseBottomSheet -> _state.update { it.copy(bottomModal = null) }
            is StatusEditorUiEvent.SetScore -> _state.update {
                it.copy(status = it.status?.copy(score = uiEvent.score))
            }

            is StatusEditorUiEvent.SetProgress -> _state.update {
                it.copy(status = it.status?.copy(progress = uiEvent.progress))
            }

            is StatusEditorUiEvent.SetStatus -> _state.update {
                it.copy(status = it.status?.copy(status = uiEvent.status))
            }

            is StatusEditorUiEvent.Save -> saveStatus(_state.value.status!!, uiEvent.onSuccess)
        }
    }

    private fun saveStatus(details: StatusDetails, onSuccess: () -> Unit) {
        if (details.status == null) {
            _state.update { it.copy(errors = it.errors + R.string.status_required) }
            return
        }

        _state.update { it.copy(isEditingPending = true) }
        viewModelScope.launch {
            updateShowStatusUseCase(showId, details)
                .handleError(::handleError)
        }.invokeOnCompletion {
            _state.update { it.copy(isEditingPending = false) }
        }
    }

    private fun handleError(errorRes: Int) = _state.update {
        it.copy(errors = it.errors + errorRes)
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }
}