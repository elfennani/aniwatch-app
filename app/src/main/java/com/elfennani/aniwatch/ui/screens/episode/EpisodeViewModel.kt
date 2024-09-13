package com.elfennani.aniwatch.ui.screens.episode

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.toRoute
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.services.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<EpisodeRoute>()

    private val episode = MutableStateFlow<String?>(null)
    private val show = MutableStateFlow<ShowDetails?>(null)

    private val _state = MutableStateFlow(EpisodeUiState())
    val state = _state.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null

    init {
        viewModelScope.launch {
            val episodeAsync = async { loadEpisode() }
            val showAsync = async { loadShow() }

            awaitAll(episodeAsync, showAsync)
        }

        viewModelScope.launch {
            combine(show, episode) { show, episode ->
                Pair(
                    show,
                    episode
                )
            }.collect { (show, episode) ->
                if (show == null || episode == null) return@collect

                val sessionToken =
                    SessionToken(context, ComponentName(context, PlaybackService::class.java))
                controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
                controllerFuture!!.addListener(
                    {
                        val mediaController = controllerFuture?.get()
                        _state.update {
                            it.copy(exoPlayer = mediaController?.apply {
                                playWhenReady = true

                                val mediaMetadata = MediaMetadata
                                    .Builder()
                                    .setTitle(show.name)
                                    .setDisplayTitle("Episode ${route.episode} • ${route.audio.name}")
                                    .setArtist(show.name)
                                    .setArtworkUri(
                                        show
                                            .episodes
                                            .find { ep -> ep.episode == route.episode.toDouble() }
                                            ?.thumbnail
                                            ?.toUri()
                                    )
                                    .build()

                                val mediaItem = MediaItem
                                    .fromUri(episode)
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
        }

        Log.d("EpisodeViewModel", "Down: Done")
    }

    private suspend fun loadEpisode() {
        if (route.useSaved) {
            val directory = File(context.filesDir, "shows/${route.id}")
            val file = File(directory, "${route.episode}.mp4")

            episode.update { file.toUri().toString() }
            return;
        }

        val res = showRepository.getEpisodeById(route.allanimeId, route.episode.toDouble(), route.audio)

        when (res) {
            is Resource.Success -> episode.update { res.data?.hls?.url }
            is Resource.Error -> _state.update { it.copy(errors = it.errors + res.message!!) }
        }
    }

    private suspend fun loadShow() {
        val localShow = showRepository.getShowFlowById(route.id).first()
        if (localShow != null) {
            show.update { localShow }
            return
        }

        when (val res = showRepository.getShowById(route.id)) {
            is Resource.Success -> show.update { res.data }
            is Resource.Error -> _state.update { it.copy(errors = it.errors + res.message!!) }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadShow()
            loadEpisode()
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