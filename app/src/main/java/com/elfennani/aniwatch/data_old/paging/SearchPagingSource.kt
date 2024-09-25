package com.elfennani.aniwatch.data_old.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elfennani.aniwatch.data_old.remote.APIService
import com.elfennani.aniwatch.data_old.remote.models.toDomain
import com.elfennani.aniwatch.domain.models.ShowBasic

class SearchPagingSource(
    val apiService: APIService,
    val query: String
): PagingSource<Int, ShowBasic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowBasic> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.searchShowsByQuery(query, nextPageNumber)

            return LoadResult.Page(
                data = response.data.map { it.toDomain() },
                prevKey = null,
                nextKey = if(response.hasNextPage) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ShowBasic>): Int = 1
}