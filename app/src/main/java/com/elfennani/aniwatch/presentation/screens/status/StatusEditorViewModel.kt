package com.elfennani.aniwatch.presentation.screens.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val showRepository: ShowRepository,
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!
    private val _state = MutableStateFlow(StatusEditorUiState())
    val state = _state.asStateFlow()

    init {
        fetchData()
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
            uiState.copy(errors=errors)
        }
    }
}