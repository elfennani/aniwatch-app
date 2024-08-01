package com.elfennani.aniwatch.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import com.elfennani.aniwatch.data.local.entities.asEntity
import com.elfennani.aniwatch.data.local.entities.toCached
import com.elfennani.aniwatch.data.local.entities.toDomain
import com.elfennani.aniwatch.data.local.entities.toDto
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.asDomain
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.data.remote.models.toSerializable
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.StatusDetails
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

class ShowRepository(
    private val apiService: APIService,
    private val watchingShowsDao: WatchingShowsDao,
    private val cachedShowDao: CachedShowDao,
    private val cachedEpisodesDao: CachedEpisodesDao,
) {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page = page)
            .map { it.toDomain() }
    }

    suspend fun getEpisodeById(allAnimeId: String, episode: Int): Resource<EpisodeLink> {
        return try {
            Resource.Success(apiService.getEpisodeById(allAnimeId, episode).toDomain())
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Resource.Error(R.string.ep_not_found)
            } else {
                Resource.Error()
            }
        } catch (e: Exception) {
            Resource.Error()
        }
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
                cachedEpisodesDao.deleteByAnimeId(showId)
                cachedEpisodesDao.insertAll(show.data.episodes.map(Episode::toCached))
            } catch (e: SQLiteException) {
                emit(Resource.Error(R.string.sql_error))
            } catch (e: Exception) {
                emit(Resource.Error())
            }
        }
    }

    suspend fun getShowById(showId: Int): Resource<ShowDetails> {
        return try {
            Resource.Success(apiService.getShowById(showId).toDomain())
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Log.e("ShowRepository", e.message.toString())
            Resource.Error(R.string.fail_parse)
        } catch (e: Exception) {
            Resource.Error()
        }
    }

    suspend fun syncWatchingShows(): Resource<Unit> {
        try {
            val shows = apiService
                .getShowsByStatus(ShowStatus.WATCHING.toSerializable(), all = true)
            Log.d("ShowRepository", shows.size.toString())
            watchingShowsDao.deleteAll()
            watchingShowsDao.insertAll(shows.map(NetworkShowBasic::toDto))
            return Resource.Success(Unit)
        } catch (e: IOException) {
            return Resource.Error(R.string.no_internet)
        } catch (e: SQLiteException) {
            return Resource.Error(R.string.sql_error)
        } catch (e: Exception) {
            return Resource.Error()
        }
    }

    fun getWatchingShows(): Flow<List<ShowBasic>> {
        return flow {
            val show = withContext(Dispatchers.IO) {
                watchingShowsDao.getShows().map(WatchingShowsDto::toDomain)
            }
            emit(show)

            val shows = withContext(Dispatchers.IO) {
                watchingShowsDao.getShowsFlow().map { it.map(WatchingShowsDto::toDomain) }
            }
            emitAll(shows)
        }
    }

    suspend fun getShowStatusById(showId: Int): Resource<StatusDetails> {
        return try {
            Resource.Success(apiService.getStatusDetailsById(showId).asDomain())
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Log.e("ShowRepository", e.message.toString())
            Resource.Error(R.string.fail_parse)
        } catch (e: Exception) {
            Resource.Error()
        }
    }
}