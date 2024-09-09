package com.elfennani.aniwatch.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.elfennani.aniwatch.data.local.Database
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.entities.ActivityDto
import com.elfennani.aniwatch.data.local.entities.asDomain
import com.elfennani.aniwatch.data.repository.ActivityRepository
import com.elfennani.aniwatch.data.paging.FeedRemoteMediator
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.data.repository.UserRepository
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.sessionId
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val activityRepository: ActivityRepository,
    private val database: Database,
    private val userRepository: UserRepository,
    private val feedDao: FeedDao,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val shows = showRepository.getListingByStatus(ShowStatus.WATCHING)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(pageSize = 25, prefetchDistance = 0, initialLoadSize = 25,),
        remoteMediator = FeedRemoteMediator(activityRepository, database, feedDao, context)
    ) { feedDao.pagingSource() }

    val feedPagingFlow = pager.flow
        .map { it.map(ActivityDto::asDomain) }
        .cachedIn(viewModelScope)


    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init {
        refetch()

        viewModelScope.launch {
            val sessionId = context.dataStore.data.first().sessionId
            userRepository.viewerFlow().collect{
                when(it){
                    is Resource.Success -> _state.update { state->
                        state.copy(user = it.data)
                    }
                    is Resource.Error -> _state.update { state -> state.copy(
                        errors = state.errors + it.message!!
                    ) }
                }
            }
        }

        viewModelScope.launch {
            shows.collect { shows ->
                _state.update {
                    Log.d("HomeViewModel", shows?.size.toString())
                    it.copy(
                        shows = shows ?: emptyList(),
                        isLoading = if (shows == null) it.isLoading else false
                    )
                }
            }
        }
    }

    fun refetch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isFetching = true) }
                val result = showRepository.syncListingByStatus(ShowStatus.WATCHING)
                _state.update { it.copy(isFetching = false) }

                if (result is Resource.Error) {
                    _state.update { it.copy(errors = it.errors + result.message!!) }
                }
            }
        }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors=errors)
        }
    }
}