package com.elfennani.aniwatch.domain.usecases


import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.usecases.result.Result
import com.elfennani.aniwatch.domain.usecases.result.GetShowsError
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.repository.ShowRepository
import retrofit2.HttpException

class GetShowsByStatusUseCase(private val showRepository: ShowRepository) {
    suspend operator fun invoke(status: ShowStatus, page: Int): Resource<List<ShowBasic>> {
        return try {
            Resource.Success(showRepository.getShowsByStatus(status, page))
        } catch (e: HttpException) {
            Resource.Error("Server Error")
        } catch (e: Exception){
            Resource.Error("Something went wrong")
        }
    }
}

