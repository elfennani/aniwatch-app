package com.elfennani.aniwatch.ui.screens.relations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelationViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val showId = savedStateHandle.get<Int>("showId")!!

    private val _state = MutableStateFlow(RelationUiState())
    val state = _state.asStateFlow()

    init {
        fetchRelations()
    }

    private fun fetchRelations(){
        viewModelScope.launch {
            when(val result = showRepository.getRelationsByShowId(showId)){
                is Resource.Error -> _state.update { it.copy(errors = it.errors + result.message!!) }
                is Resource.Success -> _state.update { it.copy(relations = result.data!!) }
            }
        }
    }

    fun dismissError(errorRes: Int) {
        _state.update { uiState ->
            val errors = uiState.errors.filterNot { it == errorRes }
            uiState.copy(errors = errors)
        }
    }
}