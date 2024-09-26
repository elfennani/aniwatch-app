package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.StatusDetails
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun showById(id: Int): Flow<Show>
    suspend fun fetchShowById(id: Int)

    fun statusDetailsById(id: Int): Flow<StatusDetails>
    suspend fun fetchStatusDetailsBy(id: Int)

    fun relationsById(id: Int): Flow<List<Show>>
    suspend fun fetchRelationsById(id: Int)
}