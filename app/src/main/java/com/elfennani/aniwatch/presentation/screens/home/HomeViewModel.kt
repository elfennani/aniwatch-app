package com.elfennani.aniwatch.presentation.screens.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.elfennani.aniwatch.data.local.Database
import com.elfennani.aniwatch.data.local.entities.asDomain
import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.repository.ShowRepository
import com.elfennani.aniwatch.domain.usecases.GetShowsByStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
) : ViewModel() {
    val shows = showRepository.getWatchingShows().shareIn(
        viewModelScope,
        SharingStarted.Lazily,
    ).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = showRepository.syncWatchingShows()
                if (result is Resource.Error) {
                    _error.value = result.message
                }
            }
        }
    }
}