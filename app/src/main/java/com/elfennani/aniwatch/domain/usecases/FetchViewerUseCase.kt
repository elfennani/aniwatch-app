package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchViewerUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<Unit, AppError> {
        return withContext(Dispatchers.IO){
            try {
                userRepository.fetchViewer()
                Resource.Ok(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Err(e.handleAppErrors())
            }
        }
    }
}