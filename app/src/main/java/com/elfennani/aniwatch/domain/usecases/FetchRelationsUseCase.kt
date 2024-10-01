package com.elfennani.aniwatch.domain.usecases

import android.util.Log
import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import javax.inject.Inject

class FetchRelationsUseCase @Inject constructor(private val showRepository: ShowRepository) {
    suspend operator fun invoke(showId: Int): Resource<Unit, AppError> {
        return try {
            Log.d("FetchRelationsUseCase", "invoke: fetching..")
            showRepository.fetchRelationsById(showId)
            Resource.Ok(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Err(e.handleAppErrors())
        }
    }
}