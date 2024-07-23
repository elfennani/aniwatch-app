package com.elfennani.aniwatch.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import com.elfennani.aniwatch.data.local.entities.asEntity
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
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException

class ShowRepository(
    private val apiService: APIService,
    private val watchingShowsDao: WatchingShowsDao,
    private val cachedShowDao: CachedShowDao
) {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page = page)
            .map { it.toDomain() }
    }

    fun getShowByIdCached(showId: Int): Flow<Resource<ShowDetails>> = flow {
        val cachedShow = cachedShowDao.getCachedShow(showId)
        if (cachedShow != null) {
            emit(Resource.Success(cachedShow.toDomain()))
        }

        val show = getShowById(showId = showId)
        emit(show)

        if (show is Resource.Success && show.data != null) {
            try {
                cachedShowDao.insertCachedShow(show.data.asEntity())
            } catch (e: SQLiteException) {
                emit(Resource.Error("Failed to save to cache"))
            } catch (e: Exception) {
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    suspend fun getShowById(showId: Int): Resource<ShowDetails> {
        return try {
            Resource.Success(apiService.getShowById(showId).toDomain())
        } catch (e: IOException) {
            Resource.Error("Internet connection error")
        } catch (e: JsonDataException) {
            Log.e("ShowRepository", e.message.toString())
            Resource.Error("Parsing error")
        } catch (e: Exception) {
            Resource.Error("Something went wrong")
        }
    }

    suspend fun syncWatchingShows(): Resource<Unit> {
        try {
            val shows = apiService
                .getShowsByStatus(ShowStatus.WATCHING.toSerializable(), all = true)
            watchingShowsDao.deleteAll()
            watchingShowsDao.insertAll(shows.map(SerializableShowBasic::toDto))
            return Resource.Success(Unit)
        } catch (e: IOException) {
            return Resource.Error("Internet connection error")
        } catch (e: SQLiteException) {
            return Resource.Error("Failed to sync")
        } catch (e: Exception) {
            return Resource.Error("Something went wrong")
        }
    }

    fun getWatchingShows(): Flow<List<ShowBasic>> {
        return watchingShowsDao.getShows().map { it.map(WatchingShowsDto::toDomain) }
    }
}