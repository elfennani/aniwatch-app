package com.elfennani.aniwatch.data.local

import androidx.room.TypeConverter
import com.elfennani.aniwatch.domain.models.Tag
import com.elfennani.aniwatch.domain.models.VoiceActor
import com.elfennani.aniwatch.domain.models.enums.ActivityType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.Instant

class Converters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
    )

    private val vaListAdapter = moshi.adapter<List<VoiceActor>>(
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
        return list?.let { stringListAdapter.toJson(list) }
    }

    @TypeConverter
    fun toListString(json: String?): List<String>? {
        return json?.let { stringListAdapter.fromJson(json) }
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

    @TypeConverter
    fun fromVAList(list:List<VoiceActor>?): String? = vaListAdapter.toJson(list)

    @TypeConverter
    fun toVAList(json: String?) = json?.let { vaListAdapter.fromJson(it) }
}