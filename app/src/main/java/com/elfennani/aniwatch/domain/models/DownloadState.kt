package com.elfennani.aniwatch.domain.models

import androidx.annotation.StringRes


sealed class DownloadState {
    data object NotSaved : com.elfennani.aniwatch.domain.models.DownloadState()
    data object Pending : com.elfennani.aniwatch.domain.models.DownloadState()
    data class Downloaded(val audio: com.elfennani.aniwatch.domain.models.EpisodeAudio) : com.elfennani.aniwatch.domain.models.DownloadState()
    data class Downloading(val progress: Float) : com.elfennani.aniwatch.domain.models.DownloadState()
    data class Failure(@StringRes val message: Int) : com.elfennani.aniwatch.domain.models.DownloadState()
}

fun com.elfennani.aniwatch.domain.models.DownloadState.toLabel() = when (this) {
    is com.elfennani.aniwatch.domain.models.DownloadState.Downloaded -> "Downloaded"
    is _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Downloading -> "Downloading"
    is _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Failure -> "Failed"
    _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.NotSaved -> "Not Saved"
    _root_ide_package_.com.elfennani.aniwatch.domain.models.DownloadState.Pending -> "Pending"
}