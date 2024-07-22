package com.elfennani.aniwatch.data.repository

import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import com.elfennani.aniwatch.data.local.entities.toDomain
import com.elfennani.aniwatch.data.local.entities.toDto
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.SerializableShowBasic
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.data.remote.models.toSerializable
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException

class ShowRepository(
    private val apiService: APIService,
    private val watchingShowsDao: WatchingShowsDao
) {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page = page).map { it.toDomain() }
    }

    suspend fun getShowById(showId: Int): ShowDetails {
        return apiService.getShowById(showId).toDomain()
    }

    suspend fun syncWatchingShows(): Resource<Unit> {
        try {
            val shows =
                apiService.getShowsByStatus(ShowStatus.WATCHING.toSerializable(), all = true)
            watchingShowsDao.deleteAll()
            watchingShowsDao.insertAll(shows.map(SerializableShowBasic::toDto))
            return Resource.Success(Unit)
        }catch (e: IOException){
            return Resource.Error("Internet connection error")
        }catch (e: Exception){
            return Resource.Error("Something went wrong")
        }
    }

    fun getWatchingShows(): Flow<List<ShowBasic>> {
        return watchingShowsDao.getShows().map { it.map(WatchingShowsDto::toDomain) }
    }
}