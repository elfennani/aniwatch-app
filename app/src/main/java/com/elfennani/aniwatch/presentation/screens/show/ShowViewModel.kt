package com.elfennani.aniwatch.presentation.screens.show

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!
    private val _state = MutableStateFlow(ShowUiState())

    val state: StateFlow<ShowUiState> = _state

    init {
        fetchShow()
    }

    private fun fetchShow() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            Log.e("ShowViewModel", "Fetching show")
            showRepository.getShowByIdCached(showId).collect { result ->
                Log.e("ShowViewModel", result.toString())
                _state.update {
                    when (result) {
                        is Resource.Success -> it.copy(show = result.data, isLoading = false)
                        is Resource.Error -> it.copy(error = result.message, isLoading = false)
                    }
                }
            }
            Log.e("ShowViewModel", "Fetched show")
        }
    }

}