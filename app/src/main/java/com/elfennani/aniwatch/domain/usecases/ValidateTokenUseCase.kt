package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Result
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

class ValidateTokenUseCase(private val sessionRepository: SessionRepository) {

    suspend operator fun invoke(accessToken:String, expirationDuration: Long): Result<Unit, AppError> {
        return withContext(Dispatchers.IO) {
            try {
                val expires = (Instant.now().epochSecond + expirationDuration) * 1000

                sessionRepository.addNewSession(accessToken, expires)
                Result.Ok(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Err(e.handleAppErrors())
            }
        }
    }

}