package com.elfennani.aniwatch.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.elfennani.anilist.FeedQuery
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.ActivityDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asEntity
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.paging.FeedMediator
import com.elfennani.aniwatch.domain.repositories.FeedRepository
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    @AniListApolloClient val apolloClient: ApolloClient,
    private val sessionRepository: SessionRepository,
    private val activityDao: ActivityDao,
    private val userDao: UserDao,
    private val showDao: ShowDao,
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
) : FeedRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val lazyFeed: Flow<PagingData<Activity>> = Pager(
        config = PagingConfig(pageSize = 25),
        remoteMediator = FeedMediator(this, dataStore)
    ) {
        activityDao.pagingSource()
    }.flow.map { pagingData ->
        pagingData.map { activity ->
            val user = userDao.getById(activity.userId)!!.asAppModel()
            val show = activity.showId?.let { showDao.getById(it) }?.asAppModel()

            activity.asAppModel(
                user = user,
                show = show
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchFeedByPage(page: Int, clearAll: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val session = sessionRepository.getCurrentSession()!!

            val response = apolloClient.query(FeedQuery(page, session.userId)).execute()
            val hasNextPage =
                response.data?.activitiesPage?.pageInfo?.pageInfoFragment?.hasNextPage ?: false
            val users = response.data?.activitiesPage?.activities
                ?.mapNotNull {
                    val listUser = it?.onListActivity?.listActivityFragment?.user?.userFragment
                    val textUser = it?.onTextActivity?.textActivityFragment?.user?.userFragment

                    listUser ?: textUser
                }
                ?.map { it.asEntity() }
            val shows = response.data?.activitiesPage?.activities
                ?.mapNotNull { it?.onListActivity?.listActivityFragment?.media?.showFragment }
                ?.map { it.asEntity() }

            val activities = response.data?.activitiesPage?.activities
                ?.filter { activity -> activity?.onTextActivity != null || activity?.onListActivity != null }
                ?.map { activity ->
                    if (activity?.onTextActivity != null) {
                        activity.onTextActivity.textActivityFragment.asEntity()
                    } else if (activity?.onListActivity != null) {
                        activity.onListActivity.listActivityFragment.asEntity()
                    } else throw Exception()
                }

            if (activities != null)
                database.withTransaction {
                    if (clearAll) {
                        activityDao.deleteAll()
                    }
                    activityDao.upsert(activities)
                    userDao.upsert(users ?: emptyList())
                    showDao.upsert(shows ?: emptyList())
                }

            return@withContext hasNextPage
        }
    }
}