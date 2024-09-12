package com.elfennani.aniwatch.models

import android.content.Context
import androidx.annotation.StringRes
import com.elfennani.aniwatch.R

class ResourceException(@StringRes val errorResource: Int) : Exception()

sealed class Resource<T>(val data: T? = null, @StringRes val message: Int? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(@StringRes message: Int = R.string.something_wrong, data: T? = null) :
        Resource<T>(data, message)
}

fun <T> Resource<T>.dataOrThrow(context: Context) = when (this) {
    is Resource.Success -> data!!
    is Resource.Error -> throw ResourceException(message!!)
}