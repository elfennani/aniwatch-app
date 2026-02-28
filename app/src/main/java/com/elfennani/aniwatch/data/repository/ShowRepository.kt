package com.elfennani.aniwatch.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.ui.util.fastAny
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.dao.ListingDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.entities.ListingItemEntity
import com.elfennani.aniwatch.data.local.entities.LocalEpisodeEntity
import com.elfennani.aniwatch.data.local.mappers.asEntity
import com.elfennani.aniwatch.data.local.mappers.toCached
import com.elfennani.aniwatch.data.local.mappers.toDomain
import com.elfennani.aniwatch.data.local.mappers.toDto
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
    private val listingDao: ListingDao,
    private val showDao: ShowDao,
    private val episodeDao: EpisodeDao,
) {
    suspend fun getShowsByStatus(status: ShowStatus) = resourceOf {
        apiService.getShowsByStatus(status.toSerializable()).map { it.toDomain() }
    }

    suspend fun getRelationsByShowId(showId: Int) = resourceOf {
        apiService.getRelationsByShowId(showId).map { it.asDomain() }
    }

    fun getDownloads(): Flow<List<ShowDetails>> = showDao
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
        showDao.insertCachedShow(show.asEntity())
        episodeDao.deleteByShowIdAndIds(showId, show.episodes.map { it.id })
        episodeDao.insertAll(show.episodes.map(Episode::toCached))
    }

    fun getShowFlowById(showId: Int): Flow<ShowDetails?> =
        showDao.getCachedShow(showId).map { it?.toDomain() }

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
        listingDao.deleteUnused(status, shows.map { it.id })
        listingDao.upsertAll(shows.map(NetworkShowBasic::toDto))
    }

    fun getListingByStatus(status: ShowStatus): Flow<List<ShowBasic>> {

        return flow {
            val show = withContext(Dispatchers.IO) {
                listingDao.getShowsByStatus(status).map(ListingItemEntity::toDomain)
            }
            emit(show)

            val shows = withContext(Dispatchers.IO) {
                listingDao.getShowsByStatusFlow(status)
                    .map { it.map(ListingItemEntity::toDomain) }
            }
            emitAll(shows)
        }
    }

    suspend fun getShowStatusById(showId: Int): Resource<StatusDetails> = resourceOf {
        apiService.getStatusDetailsById(showId).asDomain()
    }

    suspend fun linkFileToEpisode(showId: Int, episode: Double, uri: Uri) {
        episodeDao.upsertLocalEpisode(
            LocalEpisodeEntity(
                showId = showId,
                episode = episode,
                uri = uri.toString()
            )
        )
    }

    suspend fun unlinkFileFromEpisode(showId: Int, episode: Double){
        episodeDao.deleteLocalEpisode(showId, episode)
    }
}