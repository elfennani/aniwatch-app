package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.StatusDate

data class NetworkDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

fun NetworkDate.asDomain() = StatusDate(year = year, month = month, day = day)
fun StatusDate.asNetwork() = NetworkDate(year = year, month = month, day = day)