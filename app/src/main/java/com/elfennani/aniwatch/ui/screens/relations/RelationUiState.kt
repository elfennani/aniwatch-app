package com.elfennani.aniwatch.ui.screens.relations

import androidx.compose.runtime.Immutable
import com.elfennani.aniwatch.domain.models.Relation
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.enums.RelationType

@Immutable
data class RelationUiState(
    val relations: List<Pair<RelationType, Show>> = emptyList(),
    val errors: List<Int> = emptyList(),
    val isLoading: Boolean = true
)
