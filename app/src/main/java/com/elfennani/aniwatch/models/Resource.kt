package com.elfennani.aniwatch.models

import androidx.annotation.StringRes
import com.elfennani.aniwatch.R

sealed class Resource<T>(val data: T? = null, @StringRes val message: Int? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(@StringRes message: Int = R.string.something_wrong, data: T? = null): Resource<T>(data, message)
}