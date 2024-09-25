package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Result
import com.elfennani.aniwatch.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchViewerUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<Unit, AppError> {
        return withContext(Dispatchers.IO){
            try {
                userRepository.fetchViewer()
                Result.Ok(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Err(e.handleAppErrors())
            }
        }
    }
}