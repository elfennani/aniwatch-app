package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.StatusDetails

data class NetworkStatusDetails(
    val status: SerializableShowStatus?,
    val score: Int,
    val progress: Int,
    val favorite: Boolean,
    val startedAt: NetworkDate?,
    val completedAt: NetworkDate?,
)

fun NetworkStatusDetails.asDomain() = StatusDetails(
    status = status?.toDomain(),
    score = score,
    progress = progress,
    favorite = favorite,
    startedAt = startedAt?.asDomain(),
    completedAt = completedAt?.asDomain()
)

fun StatusDetails.asNetwork() = NetworkStatusDetails(
    status = status?.toSerializable(),
    score = score,
    progress = progress,
    favorite = favorite,
    startedAt = startedAt?.asNetwork(),
    completedAt = completedAt?.asNetwork()
)