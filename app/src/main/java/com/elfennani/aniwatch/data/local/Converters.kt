package com.elfennani.aniwatch.data.local

import androidx.room.TypeConverter
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowStatus
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromShowStatus(showStatus: ShowStatus?): String? {
        return showStatus?.name
    }

    @TypeConverter
    fun toShowStatus(name: String?): ShowStatus? {
        return name?.let { ShowStatus.valueOf(it) }
    }
}