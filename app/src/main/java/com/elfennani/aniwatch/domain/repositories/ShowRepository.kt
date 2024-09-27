package com.elfennani.aniwatch.domain.repositories

import androidx.paging.PagingData
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.enums.RelationType
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun showById(id: Int): Flow<Show>
    suspend fun fetchShowById(id: Int)

    fun relationsById(showId: Int): Flow<List<Pair<RelationType, Show>>>
    suspend fun fetchRelationsById(showId: Int)

    fun charactersById(showId: Int): Flow<PagingData<Character>>
    suspend fun fetchCharactersById(showId: Int, page: Int): Boolean
}