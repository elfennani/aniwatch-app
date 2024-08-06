package com.elfennani.aniwatch.presentation.screens.show

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!
    private val _state = MutableStateFlow(ShowUiState())
    private val _show =
        showRepository.getShowFlowById(showId).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val state: StateFlow<ShowUiState> = combine(_state,_show) { state,show ->
        state.copy(
            show = show,
            isLoading = show == null
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ShowUiState())

    init {
        syncShow()
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }

    private fun syncShow() {
        viewModelScope.launch {
            val sync = showRepository.syncShowById(showId)
            if (sync is Resource.Error) {
                _state.update {
                    it.copy(errors = it.errors + sync.message!!)
                }
            }
        }
    }
}