package com.elfennani.aniwatch.domain.usecases.result

import com.elfennani.aniwatch.domain.RootError

sealed interface Result<out D, out E: RootError> {
    class Success<out D, out E: RootError>(val data: D): Result<D, E>
    class Error<out D, out E: RootError>(val error: E): Result<D, E>
}