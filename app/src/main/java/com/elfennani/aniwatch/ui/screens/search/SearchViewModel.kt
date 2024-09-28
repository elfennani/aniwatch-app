package com.elfennani.aniwatch.ui.screens.search

import androidx.lifecycle.ViewModel
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    showRepository: ShowRepository,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val listing = query.debounce(1000).flatMapLatest {
        showRepository.showsBySearchQuery(it)
    }

    fun setQuery(query: String) {
        _query.update { query }
    }
}