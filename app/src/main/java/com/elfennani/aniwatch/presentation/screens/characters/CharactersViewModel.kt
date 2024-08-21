package com.elfennani.aniwatch.presentation.screens.characters

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.elfennani.aniwatch.data.paging.CharacterPagingSource
import com.elfennani.aniwatch.data.remote.APIService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    apiService: APIService,
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val showId = savedStateHandle.get<Int>("showId")!!
    private val pager = Pager(PagingConfig(pageSize = 25, initialLoadSize = 25)){
        CharacterPagingSource(apiService, context, showId)
    }

    val charactersFlow = pager.flow.cachedIn(viewModelScope)
}