package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.models.ShowDetails
import com.elfennani.aniwatch.domain.repository.ShowRepository

class GetShowByIdUseCase(private val showRepository: ShowRepository) {
    suspend operator fun invoke(showId: Int): ShowDetails {
        return showRepository.getShowById(showId)
    }
}