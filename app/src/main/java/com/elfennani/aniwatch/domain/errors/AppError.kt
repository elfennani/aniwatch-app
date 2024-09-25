package com.elfennani.aniwatch.domain.errors

import android.database.sqlite.SQLiteException
import com.squareup.moshi.JsonDataException
import java.io.IOException

sealed interface AppError {
    data object NetworkError : AppError
    data object ServerError : AppError
    data object NotFoundError : AppError
    data object ParsingError : AppError
    data object CachingError : AppError
    data object UnknownError : AppError

    companion object {
        fun Exception.handleAppErrors(): AppError {
            return when (this) {
                is IOException -> NetworkError
                is retrofit2.HttpException -> if (code() == 404) NotFoundError else ServerError
                is JsonDataException -> ParsingError
                is SQLiteException -> CachingError
                else -> UnknownError
            }
        }
    }
}