package com.elfennani.aniwatch.ui.screens.episode

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.toRoute
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.services.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<EpisodeRoute>()

    private var didUpdateProgress = false
    private val progress = MutableStateFlow(0L)
    private val episode = MutableStateFlow<String?>(null)
    private val show = MutableStateFlow<Show?>(null)

    private val _state = MutableStateFlow(EpisodeUiState())
    val state = _state.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null


    fun refresh() {
        viewModelScope.launch {

        }
    }

    override fun onCleared() {
//        state.value.exoPlayer?.release()
        state.value.exoPlayer?.stop()
        val stopIntent = Intent(context, PlaybackService::class.java).setAction("STOP_SERVICE")
        context.startService(stopIntent)
        if (controllerFuture != null) MediaController.releaseFuture(controllerFuture!!)
        super.onCleared()
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }
}