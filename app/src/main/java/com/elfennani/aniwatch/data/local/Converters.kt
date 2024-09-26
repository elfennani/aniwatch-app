package com.elfennani.aniwatch.data.local

import androidx.room.TypeConverter
import com.elfennani.aniwatch.domain.models.Tag
import com.elfennani.aniwatch.domain.models.enums.ActivityType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.Instant

class Converters {
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
    )

    @TypeConverter
    fun fromLongToInstant(long: Long) = Instant.fromEpochMilliseconds(long)

    @TypeConverter
    fun fromInstantToLong(instant: Instant) = instant.toEpochMilliseconds()

    @TypeConverter
    fun toActivityType(string: String) = ActivityType.valueOf(string)

    @TypeConverter
    fun fromActivityType(activityType: ActivityType) = activityType.name

    @TypeConverter
    fun fromListString(list: List<String>?): String? {
        return list?.let { adapter.toJson(list) }
    }

    @TypeConverter
    fun toListString(json: String?): List<String>? {
        return json?.let { adapter.fromJson(json) }
    }

    @TypeConverter
    fun fromTagsList(tags: List<Tag>): String {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val adapter = moshi
            .adapter<List<Tag>>(
                Types.newParameterizedType(List::class.java, Tag::class.java)
            )

        return adapter.toJson(tags)
    }

    @TypeConverter
    fun toTagsList(json: String): List<Tag> {
        try {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val adapter = moshi
                .adapter<List<Tag>>(
                    Types.newParameterizedType(List::class.java, Tag::class.java)
                )

            return adapter.fromJson(json)!!
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}