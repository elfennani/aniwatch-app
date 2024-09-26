package com.elfennani.aniwatch.ui.screens.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.toLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
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
            val map = mutableMapOf<String, List<Pair<Show, Episode>>>()
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