package com.elfennani.aniwatch.ui.screens.downloads

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Paint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.DownloadState
import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.toLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okhttp3.internal.toImmutableMap
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    val showRepository: ShowRepository,
) : ViewModel() {

    private val shows = showRepository
        .getDownloads()
        .map { shows ->
            val map = mutableMapOf<String, List<Pair<ShowDetails, Episode>>>()
            shows.forEach { show ->
                show.episodes.forEach { episode ->
                    if (episode.state !is _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.NotSaved) {
                        val list = map[episode.state.toLabel()] ?: emptyList()
                        map[episode.state.toLabel()] = list + Pair(show, episode)
                    }
                }
            }

            map.toImmutableMap()
        }

    val downloads = shows.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    private fun getDownloads() {
        TODO()
    }

    fun forceStartDownload() {
        TODO()
    }

    fun clearDownloads() {
        TODO()
    }
}