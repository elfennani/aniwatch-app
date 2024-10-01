package com.elfennani.aniwatch.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.readable
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.handleError
import com.elfennani.aniwatch.domain.repositories.FeedRepository
import com.elfennani.aniwatch.domain.repositories.ListingRepository
import com.elfennani.aniwatch.domain.repositories.UserRepository
import com.elfennani.aniwatch.domain.usecases.FetchListingUseCase
import com.elfennani.aniwatch.domain.usecases.FetchViewerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
    private val fetchViewerUseCase: FetchViewerUseCase,
    private val fetchListingUseCase: FetchListingUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val shows = listingRepository.listingByStatus(ShowStatus.WATCHING)
    private val user = userRepository.viewer
    val lazyFeed = feedRepository.lazyFeed.cachedIn(viewModelScope)

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = combine(_state, shows, user) { state, shows, user ->
        state.copy(
            shows = shows,
            user = user,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            val listingAsync = async {
                fetchListingUseCase(ShowStatus.WATCHING)
                    .handleError(::handleError)
            }
            val userAsync = async {
                fetchViewerUseCase()
                    .handleError(::handleError)
            }

            awaitAll(listingAsync, userAsync)
        }
    }

    private fun handleError(errorRes: Int) = _state.update {
        it.copy(errors = it.errors + errorRes)
    }

    fun onRefresh() {
        viewModelScope.launch {
            val listingAsync = async { listingRepository.listingByStatus(ShowStatus.WATCHING) }
            val userAsync = async { userRepository.fetchViewer() }

            awaitAll(listingAsync, userAsync)
        }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }
}