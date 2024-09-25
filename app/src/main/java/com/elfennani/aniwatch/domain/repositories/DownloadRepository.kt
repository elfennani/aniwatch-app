package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    // TODO: Rework and make a different download model
    val downloads: Flow<DownloadState>
}