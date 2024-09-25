package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Result
import com.elfennani.aniwatch.domain.repositories.UserRepositoryOld
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchUserByIdUseCase(private val userRepository: UserRepositoryOld) {
    suspend operator fun invoke(id: Int): Result<Unit, AppError> {
        return withContext(Dispatchers.IO) {
            try {
                userRepository.fetchUserById(id)
                Result.Ok(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Err(e.handleAppErrors())
            }
        }
    }
}