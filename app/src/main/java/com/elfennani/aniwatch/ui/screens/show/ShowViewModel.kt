package com.elfennani.aniwatch.ui.screens.show

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.os.bundleOf
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data.local.entities.LocalDownloadedEpisode
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.EpisodeState
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.services.DownloadService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val downloadRepository: DownloadRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ShowRoute>()
    private val showId = route.id

    private val isSubDefault = context.dataStore.data
        .map { it[booleanPreferencesKey("default-audio-sub")] }

    private val _state = MutableStateFlow(ShowUiState())
    private val _show = showRepository
        .getShowFlowById(showId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val state: StateFlow<ShowUiState> =
        combine(_state, _show, isSubDefault) { state, show, isSubDefault ->
            state.copy(
                show = show?.copy(episodes = show.episodes),
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
        viewModelScope.launch { downloadRepository.deleteDownload(showId, episode) }
            .invokeOnCompletion {
                val directory = File(context.filesDir, "shows/$showId")
                val file = File(directory, "$episode.mp4")

                file.delete()
            }
    }

    fun downloadEpisode(episode: Int, audio: EpisodeAudio) {
        viewModelScope.launch {
            downloadRepository.addDownload(showId, episode, audio)
        }

        val intent = Intent(context, DownloadService::class.java)
            .putExtra("showId", showId)
            .putExtra("episode", episode)
            .putExtra("audio", audio.name)

        context.startForegroundService(intent)
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