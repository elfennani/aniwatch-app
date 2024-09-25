package com.elfennani.aniwatch.ui.screens.relations

import androidx.compose.runtime.Immutable
import com.elfennani.aniwatch.domain.models.Relation

@Immutable
data class RelationUiState(
    val relations: List<Relation> = emptyList(),
    val errors: List<Int> = emptyList()
)
