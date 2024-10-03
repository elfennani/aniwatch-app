package com.elfennani.aniwatch.domain.errors

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.annotation.StringRes
import com.elfennani.aniwatch.R
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
                is AppErrorException -> error
                is IOException -> NetworkError
                is retrofit2.HttpException -> if (code() == 404) NotFoundError else ServerError
                is JsonDataException -> ParsingError
                is SQLiteException -> CachingError
                else -> UnknownError
            }
        }

        fun AppError.readable(
            @StringRes caching: Int = R.string.sql_error,
            @StringRes network: Int = R.string.no_internet,
            @StringRes notFound: Int = R.string.not_found,
            @StringRes parsing: Int = R.string.fail_parse,
            @StringRes server: Int = R.string.something_wrong,
            @StringRes unknown: Int = R.string.something_wrong,
        ): Int = when (this) {
            CachingError -> caching
            NetworkError -> network
            NotFoundError -> notFound
            ParsingError -> parsing
            ServerError -> server
            UnknownError -> unknown
        }

        fun AppError.readable(
            context: Context,
            @StringRes caching: Int = R.string.sql_error,
            @StringRes network: Int = R.string.no_internet,
            @StringRes notFound: Int = R.string.not_found,
            @StringRes parsing: Int = R.string.fail_parse,
            @StringRes server: Int = R.string.something_wrong,
            @StringRes unknown: Int = R.string.something_wrong,
        ): String = context.getString(
            this.readable(
                caching,
                network,
                notFound,
                parsing,
                server,
                unknown,
            )
        )
    }
}