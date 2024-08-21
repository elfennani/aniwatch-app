package com.elfennani.aniwatch.utils

import android.database.sqlite.SQLiteException
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.Resource
import com.squareup.moshi.JsonDataException
import java.io.IOException

suspend fun <T> resourceOf(call: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(call())
    } catch (e: IOException) {
        e.printStackTrace()
        Resource.Error(R.string.no_internet)
    } catch (e: retrofit2.HttpException) {
        e.printStackTrace()
        Resource.Error(if (e.code() == 404) R.string.not_found else R.string.something_wrong)
    } catch (e: JsonDataException) {
        e.printStackTrace()
        Resource.Error(R.string.fail_parse)
    } catch (e: SQLiteException) {
        e.printStackTrace()
        Resource.Error(R.string.sql_error)
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error()
    }
}
