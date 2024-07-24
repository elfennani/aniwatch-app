package com.elfennani.aniwatch.presentation.screens.episode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id = savedStateHandle.get<Int>("id")!!
    val allanimeId = savedStateHandle.get<String>("allanimeId")!!
    val episode = savedStateHandle.get<Int>("episode")!!

    private val _state = MutableStateFlow(EpisodeUiState())
    val state = _state.asStateFlow()

    init {
        fetchEpisode()
        fetchShow()
    }

    private fun fetchEpisode() {
        viewModelScope.launch {
            when (val result = showRepository.getEpisodeById(allanimeId, episode)) {
                is Resource.Success -> {
                    _state.update { state -> state.copy(episode = result.data) }
                }
                is Resource.Error -> {
                    _state.update { state -> state.copy(error = result.message) }
                }
            }
        }
    }

    private fun fetchShow() {
        viewModelScope.launch {
            val result = showRepository.getShowByIdCached(id)

            result.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update { state ->
                            state.copy(
                                show = result.data,
                                episodeDetails = result.data?.episodes?.find { it.episode == episode }
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update { state -> state.copy(error = result.message) }
                    }
                }
            }
        }
    }
}