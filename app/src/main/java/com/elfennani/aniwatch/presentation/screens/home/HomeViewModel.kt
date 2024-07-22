package com.elfennani.aniwatch.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.elfennani.aniwatch.data.repository.ShowPagingSource
import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.usecases.GetShowsByStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getShowsByStatusUseCase: GetShowsByStatusUseCase,
    private val showPagingSource: ShowPagingSource
): ViewModel() {
    private val _shows = MutableStateFlow<List<ShowBasic>>(listOf())
    val shows: StateFlow<List<ShowBasic>> = _shows

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val page = MutableStateFlow(1)

    val flow = Pager(PagingConfig(pageSize = 20)){
        showPagingSource
    }.flow.cachedIn(viewModelScope)

    init {
//        getShowsByStatus()
    }

    fun loadMore() {
        page.value++
        getShowsByStatus()
    }

    private fun getShowsByStatus() {
        viewModelScope.launch{
            when(val result = getShowsByStatusUseCase(ShowStatus.COMPLETED,page.value)){
                is Resource.Success -> {
                    _shows.value = result.data!!
                }
                is Resource.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

}