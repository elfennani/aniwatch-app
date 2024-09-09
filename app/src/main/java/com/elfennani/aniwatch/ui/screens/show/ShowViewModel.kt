package com.elfennani.aniwatch.ui.screens.show

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.EpisodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val downloadRepository: DownloadRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val showId = savedStateHandle.get<Int>("id")!!

    private val isSubDefault = context.dataStore.data
        .map { it[booleanPreferencesKey("default-audio-sub")] }

    private val _state = MutableStateFlow(ShowUiState())
    private val _show = showRepository
        .getShowFlowById(showId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _savedEpisodes = downloadRepository.getDownloads()

    val state: StateFlow<ShowUiState> =
        combine(
            _state,
            _show,
            _savedEpisodes,
            isSubDefault
        ) { state, show, savedEpisodes, isSubDefault ->
            state.copy(
                show = show?.copy(
                    episodes = show.episodes.map { ep ->
                        val download = savedEpisodes.find { savedEp ->
                            savedEp.episode == ep.episode && savedEp.showId == showId
                        }

                        val state = when (download?.status) {
                            DownloadStatus.PENDING -> EpisodeState.DOWNLOADING
                            DownloadStatus.COMPLETED -> EpisodeState.SAVED
                            else -> EpisodeState.NOT_SAVED
                        }

                        ep.copy(state = state)
                    }
                ),
                isLoading = show == null,
                defaultAudio = when (isSubDefault) {
                    false -> EpisodeAudio.DUB
                    else -> EpisodeAudio.SUB
                }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ShowUiState())

    init {
        syncShow()
        viewModelScope.launch {
            state.collect {
                Log.d("ShowViewModel", "state: ${state.value.show?.episodes?.size}")
            }
        }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }

    fun deleteEpisode(episode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadRepository.deleteEpisode(episode, showId)
        }
    }

    fun downloadEpisode(episode: Int, audio: EpisodeAudio) {
        viewModelScope.launch {
            downloadRepository.downloadEpisode(_show.value!!, episode, audio)
        }
    }

    fun toggleAudio() {
        viewModelScope.launch {
            context.dataStore.edit {
                it[booleanPreferencesKey("default-audio-sub")] =
                    state.value.defaultAudio != EpisodeAudio.SUB
            }
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