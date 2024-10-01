package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.repositories.ListingRepository
import javax.inject.Inject

class FetchListingUseCase @Inject constructor(private val listingRepository: ListingRepository) {
    suspend operator fun invoke(status: ShowStatus): Resource<Unit, AppError> {
        return try {
            listingRepository.fetchListingByStatus(status)
            Resource.Ok(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Err(e.handleAppErrors())
        }
    }
}