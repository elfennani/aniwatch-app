package com.elfennani.aniwatch.domain.repositories

import androidx.paging.PagingData
import com.elfennani.aniwatch.domain.models.Activity
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    val lazyFeed: Flow<PagingData<Activity>>
    suspend fun fetchFeedByPage(page: Int, clearAll:Boolean = false): Boolean
}