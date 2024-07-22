package com.elfennani.aniwatch.domain.usecases.result

enum class GetShowsError : Error {
    NO_INTERNET,
    BAD_REQUEST,
    UNKNOWN
}