package com.elfennani.aniwatch.data.repository

import android.util.Log
import androidx.compose.ui.util.fastAny
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.CachedListingDao
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.entities.CachedListingDto
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
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeAudio
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowDetails
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.StatusDetails
import com.elfennani.aniwatch.models.toNetwork
import com.elfennani.aniwatch.utils.resourceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShowRepository(
    private val apiService: APIService,
    private val cachedListingDao: CachedListingDao,
    private val cachedShowDao: CachedShowDao,
    private val cachedEpisodesDao: CachedEpisodesDao,
) {
    suspend fun getShowsByStatus(status: ShowStatus) = resourceOf {
        apiService.getShowsByStatus(status.toSerializable()).map { it.toDomain() }
    }

    suspend fun getRelationsByShowId(showId: Int) = resourceOf {
        apiService.getRelationsByShowId(showId).map { it.asDomain() }
    }

    fun getDownloads(): Flow<List<ShowDetails>> = cachedShowDao
        .getCachedShows()
        .map {
            it.map { show -> show.toDomain() }
                .filter { show ->
                    show.episodes
                        .fastAny { episode -> episode.state !is DownloadState.NotSaved }
                }
        }

    suspend fun getEpisodeById(
        allAnimeId: String,
        episode: Double,
        audio: EpisodeAudio = EpisodeAudio.SUB,
    ) = resourceOf {
        apiService.getEpisodeById(
            allAnimeId = allAnimeId,
            episode = episode,
            type = audio.toNetwork()
        ).toDomain()
    }

    suspend fun syncShowById(showId: Int) = resourceOf {
        val show = apiService.getShowById(showId).toDomain()
        cachedShowDao.insertCachedShow(show.asEntity())
        cachedEpisodesDao.deleteByShowIdAndIds(showId, show.episodes.map { it.id })
        cachedEpisodesDao.insertAll(show.episodes.map(Episode::toCached))
    }

    fun getShowFlowById(showId: Int): Flow<ShowDetails?> =
        cachedShowDao.getCachedShow(showId).map { it?.toDomain() }

    suspend fun getShowById(showId: Int) = resourceOf {
        apiService.getShowById(showId).toDomain()
    }

    suspend fun setShowStatus(showId: Int, statusDetails: StatusDetails) = resourceOf {
        apiService.setStatusDetailsById(showId, statusDetails.asNetwork())
        val sync = withContext(Dispatchers.IO) { syncShowById(showId) }
        val syncWatching =
            withContext(Dispatchers.IO) { syncListingByStatus(ShowStatus.WATCHING) }

        if (sync is Resource.Error || syncWatching is Resource.Error) {
            throw Error()
        }
    }

    suspend fun syncListingByStatus(status: ShowStatus) = resourceOf {
        val shows = apiService
            .getShowsByStatus(status.toSerializable())
        Log.d("ShowRepository", shows.size.toString())
        cachedListingDao.deleteUnused(status, shows.map { it.id })
        cachedListingDao.upsertAll(shows.map(NetworkShowBasic::toDto))
    }

    fun getListingByStatus(status: ShowStatus): Flow<List<ShowBasic>> {
        return flow {
            val show = withContext(Dispatchers.IO) {
                cachedListingDao.getShowsByStatus(status).map(CachedListingDto::toDomain)
            }
            emit(show)

            val shows = withContext(Dispatchers.IO) {
                cachedListingDao.getShowsByStatusFlow(status)
                    .map { it.map(CachedListingDto::toDomain) }
            }
            emitAll(shows)
        }
    }

    suspend fun getShowStatusById(showId: Int): Resource<StatusDetails> = resourceOf {
        apiService.getStatusDetailsById(showId).asDomain()
    }
}