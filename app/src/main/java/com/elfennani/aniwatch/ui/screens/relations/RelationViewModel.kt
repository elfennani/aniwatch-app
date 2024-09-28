package com.elfennani.aniwatch.ui.screens.relations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.domain.models.handleError
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.domain.usecases.FetchRelationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelationViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val fetchRelationsUseCase: FetchRelationsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val showId = savedStateHandle.get<Int>("showId")!!
    private val relations = showRepository.relationsById(showId)

    private val _state = MutableStateFlow(RelationUiState())
    val state = combine(_state, relations){state, relations ->
        state.copy(relations = relations, isLoading = false)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        RelationUiState()
    )

    init {
        viewModelScope.launch {
            fetchRelationsUseCase(showId).handleError(::handleError)
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
}