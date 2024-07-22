package com.elfennani.aniwatch.domain.repository

import com.elfennani.aniwatch.domain.models.ShowBasic
import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.models.ShowStatus

interface ShowRepository {
    suspend fun getShowsByStatus(status: ShowStatus, page: Int = 1): List<ShowBasic>
    suspend fun getShowById(showId: Int): ShowDetails
}