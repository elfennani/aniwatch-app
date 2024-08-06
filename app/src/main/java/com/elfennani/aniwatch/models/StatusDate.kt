package com.elfennani.aniwatch.models

import java.util.Calendar
import java.util.Locale

data class StatusDate(
    val year: Int,
    val month: Int,
    val day: Int,
){
    fun format(): String {
        val calendar = this.toCalendar();
        val day = String.format(Locale.ENGLISH,"%02d",calendar.get(Calendar.DAY_OF_MONTH))
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH)
        val year = calendar.get(Calendar.YEAR)
        return "$day $month $year"
    }
}

fun StatusDate.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, this.year)
    calendar.set(Calendar.MONTH, this.month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, this.day)

    return calendar
}

fun Long.toStatusDate(): StatusDate {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)

    return StatusDate(
        year = year, month = month, day = day
    )
}