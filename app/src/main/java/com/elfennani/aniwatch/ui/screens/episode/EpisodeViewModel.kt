package com.elfennani.aniwatch.ui.screens.episode

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.toRoute
import com.elfennani.aniwatch.domain.errors.AppError.Companion.readable
import com.elfennani.aniwatch.domain.models.EpisodeDetails
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.domain.usecases.FetchEpisodeDetailsUseCase
import com.elfennani.aniwatch.services.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val fetchEpisodeDetailsUseCase: FetchEpisodeDetailsUseCase,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<EpisodeRoute>()

    private var didUpdateProgress = false
    private val progress = MutableStateFlow(0L)
    private val show = showRepository.showById(route.showId)

    private val _state = MutableStateFlow(EpisodeUiState())
    val state = combine(_state, show) { state, show ->

        if (state.exoPlayer != null) {
            state.exoPlayer.replaceMediaItem(
                state.exoPlayer.currentMediaItemIndex,
                state.exoPlayer.currentMediaItem!!.buildUpon()
                    .setMediaMetadata(
                        state.exoPlayer.mediaMetadata.buildUpon()
                            .setTitle(show.name)
                            .setArtist(show.name)
                            .build()
                    )
                    .build()
            )
        }

        state.copy(isLoading = state.exoPlayer != null, show = show)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EpisodeUiState())

    private var controllerFuture: ListenableFuture<MediaController>? = null

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val details =
                fetchEpisodeDetailsUseCase(route.showId, route.episode.toDouble(), route.audio)

            Log.d("EpisodeViewModel", "refresh: $details")
            when (details) {
                is Resource.Err -> _state.update {
                    Log.d("EpisodeViewModel", "refresh: ${details.error}")
                    it.copy(errors = it.errors + details.error.readable())
                }

                is Resource.Ok -> launchPlayer(details.data)
            }
        }
    }

    private fun launchPlayer(episodeDetails: EpisodeDetails) {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture!!.addListener(
            {
                val mediaController = controllerFuture?.get()
                _state.update {
                    it.copy(exoPlayer = mediaController?.apply {
                        playWhenReady = true

                        val mediaMetadata = MediaMetadata
                            .Builder()
//                            .setTitle(show.name)
                            .setDisplayTitle("Episode ${route.episode} • ${route.audio.name}")
//                            .setArtist(show.name)
//                            .setArtworkUri(
//                                show
//                                    .episodes
//                                    .find { ep -> ep.episode == route.episode.toDouble() }
//                                    ?.thumbnail
//                                    ?.toUri()
//                            )
                            .build()

                        val mediaItem = MediaItem
                            .fromUri(episodeDetails.uri)
                            .buildUpon()
                            .setMediaMetadata(mediaMetadata)
                            .build()
                        addMediaItem(mediaItem)

                        play()
                    })
                }
            },
            MoreExecutors.directExecutor()
        )
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