package com.elfennani.aniwatch.domain.paging

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.elfennani.aniwatch.data.local.models.LocalActivity
import com.elfennani.aniwatch.domain.models.Activity
import com.elfennani.aniwatch.domain.paging.CharactersMediator.Companion
import com.elfennani.aniwatch.domain.repositories.FeedRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class FeedMediator(
    private val feedRepository: FeedRepository,
    private val dataStore: DataStore<Preferences>,
) : RemoteMediator<Int, LocalActivity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalActivity>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> dataStore.data.first()[FEED_PAGE_KEY]

        }

        return try {
            val hasNextPage = feedRepository.fetchFeedByPage(
                page = loadKey ?: 1,
                clearAll = loadType == LoadType.REFRESH
            )

            dataStore.edit {
                if (loadType == LoadType.REFRESH) {
                    it[LAST_UPDATED_KEY] = Clock.System.now().toEpochMilliseconds()
                }

                it[FEED_PAGE_KEY] = (loadKey ?: 1) + 1
            }

            MediatorResult.Success(endOfPaginationReached = !hasNextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES)
        val lastUpdated = dataStore.data.first()[LAST_UPDATED_KEY] ?: 0
        val now = Clock.System.now().toEpochMilliseconds()

        return if (now - lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }


    companion object {
        val FEED_PAGE_KEY = intPreferencesKey("FEED_PAGE")
        val LAST_UPDATED_KEY = longPreferencesKey("FEED_LAST_UPDATED")
    }
}