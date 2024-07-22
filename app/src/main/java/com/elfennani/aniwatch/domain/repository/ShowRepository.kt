package com.elfennani.aniwatch.domain.repository

import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowStatus
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int = 1): List<ShowBasic>
    suspend fun getShowById(showId: Int): ShowDetails
    fun getWatchingShows(): Flow<List<ShowBasic>>
    suspend fun syncWatchingShows(): Resource<Unit>
}