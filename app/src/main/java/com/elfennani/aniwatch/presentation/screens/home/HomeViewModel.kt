package com.elfennani.aniwatch.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
) : ViewModel() {
    private val shows = showRepository.getWatchingShows().shareIn(
        viewModelScope,
        SharingStarted.Lazily,
    ).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init {
        refetch()

        viewModelScope.launch {
            shows.collect { shows -> _state.update { it.copy(shows = shows) } }
        }
    }

    fun refetch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isFetching = true, error = null) }
                val result = showRepository.syncWatchingShows()
                _state.update { it.copy(isFetching = false) }

                if (result is Resource.Error) {
                    _state.update { it.copy(error = result.message) }
                }
            }
        }
    }

    fun hideError(){
        _state.update { it.copy(error = null) }
    }
}