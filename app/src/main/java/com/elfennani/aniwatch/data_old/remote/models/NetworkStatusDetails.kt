package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.StatusDetails

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