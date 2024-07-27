package com.elfennani.aniwatch.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elfennani.aniwatch.models.Activity
import com.elfennani.aniwatch.models.Resource

class FeedPagingSource(
    private val activityRepository: ActivityRepository
): PagingSource<Int, Activity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Activity> {
        val nextPage = params.key ?: 1
        return when(val resource = activityRepository.getFeed(nextPage)){
            is Resource.Success -> LoadResult.Page(
                data = resource.data ?: emptyList(),
                prevKey = null,
                nextKey = nextPage + 1
            )
            is Resource.Error -> LoadResult.Error(Exception(resource.message))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Activity>): Int? {
        return null
    }

}