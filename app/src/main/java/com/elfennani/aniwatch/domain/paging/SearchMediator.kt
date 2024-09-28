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
import com.elfennani.aniwatch.data.local.models.LocalSearch
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class SearchMediator(
    private val showRepository: ShowRepository,
    private val dataStore: DataStore<Preferences>,
    private val query: String,
) : RemoteMediator<Int, LocalSearch>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalSearch>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> (state.lastItemOrNull()?.page ?: 0) + 1
        }

        return try {
            val hasNextPage = showRepository.fetchShowsBySearchQuery(query, loadKey ?: 1)

            dataStore.edit {
                if (loadType == LoadType.REFRESH) {
                    it[LAST_UPDATED_KEY] = Clock.System.now().toEpochMilliseconds()
                }
            }

            MediatorResult.Success(endOfPaginationReached = !hasNextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES)
        val lastUpdated = dataStore.data.first()[CharactersMediator.LAST_UPDATED_KEY] ?: 0
        val now = Clock.System.now().toEpochMilliseconds()

        return if (now - lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    companion object {
        val LAST_UPDATED_KEY = longPreferencesKey("SEARCH_LAST_UPDATED")
    }
}