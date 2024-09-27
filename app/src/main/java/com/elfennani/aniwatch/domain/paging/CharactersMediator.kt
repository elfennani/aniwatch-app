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
import com.elfennani.aniwatch.data.local.models.LocalCharacter
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharactersMediator(
    private val showRepository: ShowRepository,
    private val dataStore: DataStore<Preferences>,
    private val showId: Int,
) : RemoteMediator<Int, LocalCharacter>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalCharacter>,
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> dataStore.data.first()[PAGE_KEY]
        }

        return try {
            val hasNextPage = showRepository.fetchCharactersById(showId, loadKey ?: 1)

            dataStore.edit {
                if (loadType == LoadType.REFRESH) {
                    it[LAST_UPDATED_KEY] = Clock.System.now().toEpochMilliseconds()
                }

                it[PAGE_KEY] = (loadKey ?: 1) + 1
            }

            MediatorResult.Success(endOfPaginationReached = !hasNextPage,)
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
        val PAGE_KEY = intPreferencesKey("CHARACTERS_PAGE")
        val LAST_UPDATED_KEY = longPreferencesKey("CHARACTERS_LAST_UPDATED")
    }
}