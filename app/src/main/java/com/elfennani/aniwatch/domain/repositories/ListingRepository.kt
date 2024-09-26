package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    fun listingByStatus(status: ShowStatus): Flow<List<ShowBasic>>
    suspend fun fetchListingByStatus(status: ShowStatus)
}