package com.elfennani.aniwatch.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.elfennani.aniwatch.data_old.paging.SearchPagingSource
import com.elfennani.aniwatch.data_old.remote.APIService
import com.elfennani.aniwatch.domain.models.ShowBasic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    apiService: APIService,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _pagingDataFlow = MutableStateFlow<Flow<PagingData<ShowBasic>>?>(null)
    val pagingDataFlow = _pagingDataFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(1000)
                .collect { query ->
                    _pagingDataFlow.update {
                        if (query.isEmpty())
                            null
                        else
                            Pager(PagingConfig(pageSize = 8)) {
                                SearchPagingSource(apiService, query)
                            }.flow.cachedIn(this)
                    }
                }
        }
    }

    fun setQuery(query: String) {
        _query.update { query }
    }
}