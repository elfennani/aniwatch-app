package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Int): Resource<Unit, AppError> {
        return withContext(Dispatchers.IO) {
            try {
                userRepository.fetchUserById(id)
                Resource.Ok(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Err(e.handleAppErrors())
            }
        }
    }
}