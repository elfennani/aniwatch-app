package com.elfennani.aniwatch.presentation.screens.relations

import androidx.compose.runtime.Immutable
import com.elfennani.aniwatch.models.Relation

@Immutable
data class RelationUiState(
    val relations: List<Relation> = emptyList(),
    val errors: List<Int> = emptyList()
)
