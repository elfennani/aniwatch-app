package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.StatusDetails
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun showById(id: Int): Flow<ShowDetails>
    suspend fun fetchShowById(id: Int)

    fun statusDetailsById(id: Int): Flow<StatusDetails>
    suspend fun fetchStatusDetailsBy(id: Int)

    fun relationsById(id: Int): Flow<List<ShowDetails>>
    suspend fun fetchRelationsById(id: Int)
}