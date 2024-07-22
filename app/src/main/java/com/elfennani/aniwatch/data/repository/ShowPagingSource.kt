package com.elfennani.aniwatch.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.repository.ShowRepository

class ShowPagingSource(
    private val showRepository: ShowRepository,
): PagingSource<Int, ShowBasic>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowBasic> {
        try {
            val nextPage = params.key ?: 1
            val response = showRepository.getShowsByStatus(ShowStatus.COMPLETED, nextPage)
            return LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ShowBasic>): Int? {
        return null
    }
}