package com.elfennani.aniwatch.data_old.remote.models

import com.squareup.moshi.Json

data class PagingResource<T>(
    val data: List<T>,
    val total: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "current_page") val currentPage: Int,
    @Json(name = "last_page") val lastPage: Int,
    @Json(name = "has_next_page") val hasNextPage: Boolean,
)
