package com.elfennani.aniwatch.ui.screens.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.ShowStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ListingViewModel @Inject constructor(
    private val showRepository: ShowRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ListingUiState())
    @OptIn(ExperimentalCoroutinesApi::class)
    val state = _state.flatMapLatest { state ->
        showRepository.getListingByStatus(state.status)
    }.map {
        _state.value.copy(listing = it)
    }.stateIn(viewModelScope, SharingStarted.Lazily, ListingUiState())

    init {
        viewModelScope.launch { showRepository.syncListingByStatus(_state.value.status) }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }

    fun setStatus(status: ShowStatus) {
        _state.update { state -> state.copy(status = status) }
        viewModelScope.launch{ showRepository.syncListingByStatus(status) }
    }
}