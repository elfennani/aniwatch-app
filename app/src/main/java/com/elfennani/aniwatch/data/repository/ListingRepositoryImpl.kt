package com.elfennani.aniwatch.data.repository

import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.elfennani.anilist.ShowsByStatusQuery
import com.elfennani.anilist.type.MediaListSort
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asEntity
import com.elfennani.aniwatch.data.remote.converters.enums.asRemoteModel
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.repositories.ListingRepository
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListingRepositoryImpl @Inject constructor(
    @AniListApolloClient private val apolloClient: ApolloClient,
    private val showDao: ShowDao,
    private val database: AppDatabase,
    private val sessionRepository: SessionRepository,
) : ListingRepository {

    override fun listingByStatus(status: ShowStatus) = showDao
        .getByStatusFlow(status)
        .map { it.map { show -> show.asAppModel() } }

    override suspend fun fetchListingByStatus(status: ShowStatus) {
        withContext(Dispatchers.IO) {
            val userId = sessionRepository.getCurrentSession()!!.userId
            val response = apolloClient.query(
                ShowsByStatusQuery(
                    userId = userId,
                    status = status.asRemoteModel(),
                    sort = listOf(MediaListSort.UPDATED_TIME_DESC)
                )
            ).execute()

            val shows = response.data?.collection?.lists?.first()?.entries?.mapNotNull {
                it?.media?.showFragment?.asEntity()
            } ?: emptyList()

            database.withTransaction {
                showDao.deleteByStatus(status)
                showDao.upsert(shows)
            }
        }
    }
}