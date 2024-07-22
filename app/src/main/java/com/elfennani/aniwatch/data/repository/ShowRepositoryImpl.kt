package com.elfennani.aniwatch.data.repository

import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import com.elfennani.aniwatch.data.local.entities.toDomain
import com.elfennani.aniwatch.data.local.entities.toDto
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.SerializableShowBasic
import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.data.remote.models.toSerializable
import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.repository.ShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException

class ShowRepositoryImpl(
    private val apiService: APIService,
    private val watchingShowsDao: WatchingShowsDao
) : ShowRepository {
    override suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page = page).map { it.toDomain() }
    }

    override suspend fun getShowById(showId: Int): ShowDetails {
        return apiService.getShowById(showId).toDomain()
    }

    override suspend fun syncWatchingShows(): Resource<Unit> {
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

    override fun getWatchingShows(): Flow<List<ShowBasic>> {
        return watchingShowsDao.getShows().map { it.map(WatchingShowsDto::toDomain) }
    }
}