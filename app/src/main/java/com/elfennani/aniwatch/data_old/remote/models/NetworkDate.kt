package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.StatusDate

data class NetworkDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

fun NetworkDate.asDomain() = StatusDate(year = year, month = month, day = day)
fun StatusDate.asNetwork() = NetworkDate(year = year, month = month, day = day)