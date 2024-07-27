package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.system.Os.remove
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
import kotlinx.coroutines.flow.first
import kotlin.math.floor

private const val FEED_REMOTE_KEY = "feed_remote_key"

@Suppress("FoldInitializerAndIfToElvis")
@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val activityRepository: ActivityRepository,
    private val database: Database,
    private val feedDao: FeedDao,
    private val context: Context
) : RemoteMediator<Int, ActivityDto>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ActivityDto>,
    ): MediatorResult {
        Log.d("FeedRemoteMediator", loadType.toString())
        Log.d("FeedRemoteMediator", state.toString())
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = 1
                null
            }
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = context.dataStore.data.first()[intPreferencesKey(FEED_REMOTE_KEY)]

                if(remoteKey == null){
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
                MediatorResult.Error(Exception(response.message))
            }
        }
    }
}