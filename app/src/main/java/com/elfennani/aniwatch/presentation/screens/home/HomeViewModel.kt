package com.elfennani.aniwatch.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        refetch()
    }

    fun refetch(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isFetching.value = true
                val result = showRepository.syncWatchingShows()
                _isFetching.value = false
                if (result is Resource.Error) {
                    _error.value = result.message
                }
            }
        }
    }
}