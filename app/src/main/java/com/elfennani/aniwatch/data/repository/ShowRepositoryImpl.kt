package com.elfennani.aniwatch.data.repository

import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.models.SerializableShowStatus
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.data.remote.models.toSerializable
import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.repository.ShowRepository

class ShowRepositoryImpl(
    private val apiService: APIService
) : ShowRepository {
    override suspend fun getShowsByStatus(status: ShowStatus, page: Int): List<ShowBasic> {
        return apiService.getShowsByStatus(status.toSerializable(), page).map { it.toDomain() }
    }

    override suspend fun getShowById(showId: Int): ShowDetails {
        return apiService.getShowById(showId).toDomain()
    }
}