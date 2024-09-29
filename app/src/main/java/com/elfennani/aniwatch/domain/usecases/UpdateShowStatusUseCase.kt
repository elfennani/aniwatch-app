package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.repositories.ShowRepository

class UpdateShowStatusUseCase(private val showRepository: ShowRepository) {
    suspend operator fun invoke(
        showId: Int,
        statusDetails: StatusDetails,
    ): Resource<Unit, AppError> {
        return try {
            showRepository.updateShowStatus(showId, statusDetails)
            Resource.Ok(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Err(e.handleAppErrors())
        }
    }
}