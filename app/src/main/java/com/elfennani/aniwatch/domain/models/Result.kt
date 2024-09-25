package com.elfennani.aniwatch.domain.models

sealed class Result<out S, out E> {
    data class Ok<out S>(val data: S) : Result<S, Nothing>()
    data class Err<out E>(val error: E) : Result<Nothing, E>()
}