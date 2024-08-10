package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.compose.ui.util.fastAny
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.local.entities.DownloadDto
import com.elfennani.aniwatch.data.local.entities.WatchingShowsDto
import com.elfennani.aniwatch.data.local.entities.asDomain
import com.elfennani.aniwatch.data.local.entities.asEntity
import com.elfennani.aniwatch.data.local.entities.toCached
import com.elfennani.aniwatch.data.local.entities.toDomain
import com.elfennani.aniwatch.data.local.entities.toDto
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.asDomain
import com.elfennani.aniwatch.data.remote.models.asNetwork
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.data.remote.models.toSerializable
import com.elfennani.aniwatch.models.Download
import com.elfennani.aniwatch.models.DownloadStatus
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.EpisodeState
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.StatusDetails
import com.elfennani.aniwatch.models.toNetwork
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import java.time.Instant
import java.util.Date

class ShowRepository(
    private val apiService: APIService,
    private val watchingShowsDao: WatchingShowsDao,
    private val cachedShowDao: CachedShowDao,
    private val cachedEpisodesDao: CachedEpisodesDao,
    private val downloadRepository: DownloadRepository,
) {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page = page)
            .map { it.toDomain() }
    }

    suspend fun getEpisodeById(
        allAnimeId: String,
        episode: Int,
        audio: EpisodeAudio = EpisodeAudio.SUB,
    ): Resource<EpisodeLink> {
        return try {
            Resource.Success(
                apiService.getEpisodeById(
                    allAnimeId = allAnimeId,
                    episode = episode,
                    type = audio.toNetwork()
                ).toDomain()
            )
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

    suspend fun syncShowById(showId: Int): Resource<Unit> {
        return try {
            val show = apiService.getShowById(showId).toDomain()
            cachedShowDao.insertCachedShow(show.asEntity())
            cachedEpisodesDao.deleteByAnimeId(showId)
            cachedEpisodesDao.insertAll(show.episodes.map(Episode::toCached))

            return Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Log.e("ShowRepository", e.message.toString())
            Resource.Error(R.string.fail_parse)
        } catch (e: SQLiteException) {
            return (Resource.Error(R.string.sql_error))
        } catch (e: Exception) {
            return (Resource.Error())
        }
    }

    fun getShowFlowById(showId: Int): Flow<ShowDetails?> {
        return flow {
            val savedEpisodes = downloadRepository.getDownloaded()
            emitAll(cachedShowDao.getCachedShow(showId).map {
                it?.toDomain()?.copy(
                    episodes = it.episodes
                        .map { ep ->
                            val isDownloaded = savedEpisodes.fastAny { savedEp ->
                                savedEp.episode == ep.episode && savedEp.showId == showId
                            }
                            ep.toDomain().copy(
                                state = if (isDownloaded) EpisodeState.SAVED else EpisodeState.NOT_SAVED
                            )
                        }
                )
            })
        }
    }

    suspend fun getShowById(showId: Int): Resource<ShowDetails> {
        return try {
            val show = apiService.getShowById(showId).toDomain()

            return Resource.Success(show)
        } catch (e: IOException) {
            Resource.Error(R.string.no_internet)
        } catch (e: JsonDataException) {
            Log.e("ShowRepository", e.message.toString())
            Resource.Error(R.string.fail_parse)
        } catch (e: SQLiteException) {
            return (Resource.Error(R.string.sql_error))
        } catch (e: Exception) {
            return (Resource.Error())
        }
    }

    suspend fun setShowStatus(showId: Int, statusDetails: StatusDetails): Resource<Unit> {
        return try {
            apiService.setStatusDetailsById(showId, statusDetails.asNetwork())
            val sync = withContext(Dispatchers.IO) { syncShowById(showId) }
            val syncWatching = withContext(Dispatchers.IO) { syncWatchingShows() }

            if (sync is Resource.Error) {
                return sync
            }

            if (syncWatching is Resource.Error) {
                return syncWatching
            }
            Resource.Success(Unit)
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
            Log.d("ShowRepository", "syncWatchingShows: $e")
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