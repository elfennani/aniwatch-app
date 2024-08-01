package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.system.Os.remove
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.elfennani.aniwatch.data.local.Database
import com.elfennani.aniwatch.data.local.dao.FeedDao
import com.elfennani.aniwatch.data.local.entities.ActivityDto
import com.elfennani.aniwatch.data.local.entities.asEntity
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.math.floor

private const val FEED_REMOTE_KEY = "feed_remote_key"
private const val FEED_REMOTE_LAST_UPDATE = "feed_remote_last_update"

@Suppress("FoldInitializerAndIfToElvis")
@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val activityRepository: ActivityRepository,
    private val database: Database,
    private val feedDao: FeedDao,
    private val context: Context,
) : RemoteMediator<Int, ActivityDto>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = getLastUpdated() ?: 0

        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getLastUpdated(): Long? {
        val value = withContext(Dispatchers.IO) {
            context
                .dataStore
                .data
                .first()[longPreferencesKey(FEED_REMOTE_LAST_UPDATE)]
        }

        return value
    }

    private suspend fun setLastUpdated(value: Long) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it[longPreferencesKey(FEED_REMOTE_LAST_UPDATE)] = value
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ActivityDto>,
    ): MediatorResult {
        Log.d("FeedRemoteMediator", loadType.toString())
        Log.d("FeedRemoteMediator", state.toString())
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                null
            }

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = context.dataStore.data.first()[intPreferencesKey(FEED_REMOTE_KEY)]

                if (remoteKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                remoteKey
            }
        }

        Log.d("FeedRemoteMediator", loadKey.toString())

        return when (val response = activityRepository.getFeed(loadKey ?: 1)) {
            is Resource.Success -> {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        context.dataStore.edit {
                            it.remove(intPreferencesKey(FEED_REMOTE_KEY))
                        }
                        setLastUpdated(System.currentTimeMillis())
                        feedDao.clearAll()
                    }

                    feedDao.insertAll(response.data?.map(Activity::asEntity)!!)
                }

                context.dataStore.edit {
                    it[intPreferencesKey(FEED_REMOTE_KEY)] = (loadKey ?: 1) + 1
                }

                MediatorResult.Success(endOfPaginationReached = response.data!!.isEmpty())
            }

            is Resource.Error -> {
                MediatorResult.Error(
                    Exception(
                        response.message?.let {
                            context.getString(it)
                        }
                    )
                )
            }
        }
    }
}