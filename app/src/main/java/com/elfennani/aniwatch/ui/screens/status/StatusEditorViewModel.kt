package com.elfennani.aniwatch.ui.screens.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.toStatusDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val showRepository: ShowRepository,
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!
    private val _state = MutableStateFlow(StatusEditorUiState())

    val state = _state.asStateFlow()

    init {
        fetchData()
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

            is StatusEditorUiEvent.Save -> saveStatus(state.value.status!!, uiEvent.onSuccess)
        }
    }

    private fun saveStatus(details: StatusDetails, onSuccess: () -> Unit) {
        if (details.status == null) {
            _state.update { it.copy(errors = it.errors + R.string.status_required) }
            return;
        }

        viewModelScope.launch {
            _state.update { it.copy(isEditingPending = true) }
            when (val result = showRepository.setShowStatus(showId, statusDetails = details)) {
                is Resource.Success -> {
                    onSuccess()
                    _state.update { it.copy(isEditingPending = false) }
                }
                is Resource.Error -> _state.update {
                    it.copy(
                        isEditingPending = false,
                        errors = it.errors + result.message!!
                    )
                }
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(isPending = true) }
            val show = showRepository.getShowById(showId)
            val status = showRepository.getShowStatusById(showId)

            if (show is Resource.Success && status is Resource.Success) {
                _state.update {
                    it.copy(
                        isPending = false,
                        show = show.data,
                        status = status.data,
                    )
                }
            } else {
                _state.update {
                    if (show is Resource.Error) {
                        it.copy(errors = it.errors + show.message!!)
                    } else if (status is Resource.Error) {
                        it.copy(errors = it.errors + status.message!!)
                    } else
                        it
                }
            }
        }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }
}