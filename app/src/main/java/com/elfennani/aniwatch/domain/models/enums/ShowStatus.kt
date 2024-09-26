package com.elfennani.aniwatch.domain.models.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PausePresentation
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SmartDisplay

enum class ShowStatus {
    WATCHING,
    ON_HOLD,
    COMPLETED,
    DROPPED,
    PLAN_TO_WATCH,
    REPEATING
}

fun ShowStatus?.formatText(progress: Int? = null, total: Int? = null) = when (this) {
    ShowStatus.ON_HOLD -> "Paused"
    ShowStatus.DROPPED -> "Dropped"
    ShowStatus.REPEATING -> "Rewatching"
    ShowStatus.WATCHING -> {
        if (progress != null && total != null)
            "Watching $progress/$total"
        else
            "Watching"
    }

    ShowStatus.COMPLETED -> "Finished"
    ShowStatus.PLAN_TO_WATCH -> "Planned"
    else -> "Set Status"
}

fun ShowStatus?.toIcon() = when (this) {
    ShowStatus.WATCHING -> Icons.Default.SmartDisplay
    ShowStatus.ON_HOLD -> Icons.Default.PausePresentation
    ShowStatus.DROPPED -> Icons.Default.PlaylistRemove
    ShowStatus.PLAN_TO_WATCH -> Icons.AutoMirrored.Default.PlaylistAdd
    ShowStatus.COMPLETED -> Icons.Default.Done
    ShowStatus.REPEATING -> Icons.Default.Repeat
    else -> Icons.Default.Add
}