package com.elfennani.aniwatch.presentation.screens.episode

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val downloadRepository: DownloadRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val id = savedStateHandle.get<Int>("id")!!
    val allanimeId = savedStateHandle.get<String>("allanimeId")!!
    val episode = savedStateHandle.get<Int>("episode")!!

    private val exoPlayer = ExoPlayer
        .Builder(context)
        .build()
        .apply {
            prepare()
            playWhenReady = true
            addListener(
                object : Player.Listener {
                    override fun onTracksChanged(tracks: Tracks) {
                        super.onTracksChanged(tracks)
                        for (trackGroup in tracks.groups) {
                            if (trackGroup.type == C.TRACK_TYPE_VIDEO) {
                                updateTracks(trackGroup)
                            }
                        }
                    }
                }
            )
        }

    private val _state = MutableStateFlow(EpisodeUiState(exoPlayer = exoPlayer))
    val state = _state.asStateFlow()

    init {
        fetchEpisode()
        fetchShow()
        updateState()
    }

    private fun updateTracks(trackGroup: Tracks.Group?) {
        _state.update {
            it.copy(trackGroup = trackGroup)
        }
    }

    private fun updateState() {
        viewModelScope.launch {
            while (true) {
                _state.update { it.copy(currentPosition = it.exoPlayer?.currentPosition) }
                delay(1000)
            }
        }
    }

    fun fetchEpisode() {
        viewModelScope.launch {
            val savedEpisodes = downloadRepository.getDownloaded()
            val saved = savedEpisodes.find { ep -> ep.episode == episode && ep.showId == id }

            if (saved != null && exoPlayer.currentMediaItem == null) {
                val file = File(context.filesDir, "shows/$id/$episode-${saved.audio.name}.mp4")

                val mediaItem = MediaItem
                    .fromUri(file.toUri())
                    .buildUpon()
                    .build()
                exoPlayer.replaceMediaItem(0, mediaItem)

                return@launch;
            }

            when (val result = showRepository.getEpisodeById(allanimeId, episode)) {
                is Resource.Success -> {
                    _state.update { state ->
                        Log.d("EpisodeViewModel", "fetchEpisode: ${result.data}")

                        if (exoPlayer.currentMediaItem == null && result.data?.hls != null) {
                            val mediaItem = MediaItem
                                .fromUri(result.data.hls.url)
                                .buildUpon()
                                .build()
                            exoPlayer.replaceMediaItem(0, mediaItem)
                        }

                        state.copy(episode = result.data)
                    }
                }

                is Resource.Error -> {
                    _state.update { state -> state.copy(errors = state.errors + result.message!!) }
                }
            }
        }
    }

    override fun onCleared() {
        exoPlayer.release()
        super.onCleared()
    }

    fun changeResolution(index: Int) {
        if (state.value.trackGroup == null) return;

        exoPlayer.trackSelectionParameters =
            exoPlayer.trackSelectionParameters
                .buildUpon()
                .setOverrideForType(
                    TrackSelectionOverride(state.value.trackGroup!!.mediaTrackGroup, index)
                )
                .setViewportSize(1920, 1080, false)
                .build()
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }

    private fun fetchShow() {
        viewModelScope.launch {
            val result = showRepository.getShowFlowById(id)

            result.collect {
                _state.update { state ->
                    state.copy(
                        show = it,
                        episodeDetails = it?.episodes?.find { ep -> ep.episode == episode }
                    )
                }
            }
        }
    }
}