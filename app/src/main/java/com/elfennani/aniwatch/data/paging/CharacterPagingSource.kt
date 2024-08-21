package com.elfennani.aniwatch.data.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.asDomain
import com.elfennani.aniwatch.models.Character
import com.elfennani.aniwatch.models.Resource
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.utils.resourceOf

class CharacterPagingSource(
    val apiService: APIService,
    val context: Context,
    val showId: Int,
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val nextPageNumber = params.key ?: 1
        val resource = resourceOf { apiService.getCharactersByShowId(showId, nextPageNumber) }

        return when (resource) {
            is Resource.Error -> LoadResult.Error(Error(context.resources.getString(resource.message!!)))
            is Resource.Success -> {
                val result = resource.data

                LoadResult.Page(
                    data = result!!.data.map { it.asDomain() },
                    prevKey = null,
                    nextKey = if(result.hasNextPage) result.currentPage + 1 else null
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>) = 1
}