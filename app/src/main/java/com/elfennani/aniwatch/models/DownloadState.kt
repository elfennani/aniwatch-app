package com.elfennani.aniwatch.models

import androidx.annotation.StringRes


sealed class DownloadState {
    data object NotSaved : DownloadState()
    data object Pending : DownloadState()
    data class Downloaded(val audio: EpisodeAudio) : DownloadState()
    data class Downloading(val progress: Float) : DownloadState()
    data class Failure(@StringRes val message: Int) : DownloadState()
}

fun DownloadState.toLabel() = when (this) {
    is DownloadState.Downloaded -> "Downloaded"
    is DownloadState.Downloading -> "Downloading"
    is DownloadState.Failure -> "Failed"
    DownloadState.NotSaved -> "Not Saved"
    DownloadState.Pending -> "Pending"
}