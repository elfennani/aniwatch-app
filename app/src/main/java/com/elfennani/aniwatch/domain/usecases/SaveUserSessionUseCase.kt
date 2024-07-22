package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.models.Session
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.usecases.result.Error
import com.elfennani.aniwatch.domain.usecases.result.GetShowsError
import com.elfennani.aniwatch.domain.usecases.result.Result
import retrofit2.HttpException

interface SaveUserSessionUseCase {
    suspend operator fun invoke(accessToken: String, expiration: Long): Resource<Long>
}