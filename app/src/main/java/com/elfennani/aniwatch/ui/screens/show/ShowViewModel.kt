package com.elfennani.aniwatch.ui.screens.show

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.domain.errors.AppError.Companion.readable
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.handleError
import com.elfennani.aniwatch.domain.repositories.DownloadRepository
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.domain.usecases.FetchShowUseCase
import com.elfennani.aniwatch.domain.usecases.IncrementEpisodeUseCase
import com.elfennani.aniwatch.services.DownloadService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val downloadRepository: DownloadRepository,
    private val fetchShowUseCase: FetchShowUseCase,
    private val incrementEpisodeUseCase: IncrementEpisodeUseCase,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ShowRoute>()
    private val showId = route.id

    private val isSubDefault = context.dataStore.data
        .map { it[booleanPreferencesKey("default-audio-sub")] }

    private val _state = MutableStateFlow(ShowUiState())
    private val _show = showRepository.showById(id = route.id)

    val state: StateFlow<ShowUiState> =
        combine(_state, _show, isSubDefault) { state, show, isSubDefault ->
            state.copy(
                show = show,
                isLoading = false,
                defaultAudio = isSubDefault.let {
                    if (isSubDefault != false) EpisodeAudio.SUB
                    else EpisodeAudio.DUB
                }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ShowUiState())

    init {
        viewModelScope.launch {
            fetchShowUseCase(showId).handleError(::handleError)
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

    fun deleteEpisode(episode: Double) {
        viewModelScope.launch { downloadRepository.deleteDownload(showId, episode) }
            .invokeOnCompletion {
                val directory = File(context.filesDir, "shows/$showId")
                val file = File(directory, "$episode.mp4")

                file.delete()
            }
    }

    fun downloadEpisode(episode: Double, audio: EpisodeAudio) {
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

    fun appendEpisode() {
        viewModelScope.launch {
            _state.update { it.copy(isIncrementingEpisode = true) }
            when (val result = incrementEpisodeUseCase(showId)) {
                is Resource.Err -> _state.update {
                    it.copy(
                        errors = it.errors + result.error.readable(),
                        isIncrementingEpisode = false
                    )
                }
                is Resource.Ok -> _state.update { it.copy(isIncrementingEpisode = false) }
            }
        }
    }
}