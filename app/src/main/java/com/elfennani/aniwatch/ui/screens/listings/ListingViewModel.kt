package com.elfennani.aniwatch.ui.screens.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.handleError
import com.elfennani.aniwatch.domain.repositories.ListingRepository
import com.elfennani.aniwatch.domain.usecases.FetchListingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListingViewModel @Inject constructor(
    private val listingRepository: ListingRepository,
    private val fetchListingUseCase: FetchListingUseCase,
) : ViewModel() {
    private val _status = MutableStateFlow(ShowStatus.COMPLETED)
    private val _state = MutableStateFlow(ListingUiState())
    private val _listing = _status.flatMapLatest { listingRepository.listingByStatus(it) }

    val state = combine(_state, _status, _listing) { state, status, listing ->
        state.copy(listing = listing, status = status, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListingUiState())

    init {
        viewModelScope.launch {
            _status.collectLatest {
                fetchListingUseCase(it).handleError(::handleError)
            }
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

    fun setStatus(status: ShowStatus) {
        _status.update { status }
    }
}