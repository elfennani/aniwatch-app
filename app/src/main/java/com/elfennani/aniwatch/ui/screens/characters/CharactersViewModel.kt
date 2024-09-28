package com.elfennani.aniwatch.ui.screens.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    showRepository: ShowRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val showId = savedStateHandle.get<Int>("showId")!!
    val charactersFlow = showRepository.charactersById(showId)
}