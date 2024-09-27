package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.readable

sealed class Resource<out S, out E> {
    data class Ok<out S>(val data: S) : Resource<S, Nothing>()
    data class Err<out E>(val error: E) : Resource<Nothing, E>()
}

fun Resource<Any, AppError>.handleError(onError: (error: Int) -> Unit){
    if(this is Resource.Err){
        onError(error.readable())
    }
}