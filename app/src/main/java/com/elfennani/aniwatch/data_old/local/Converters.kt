package com.elfennani.aniwatch.data_old.local

import androidx.room.TypeConverter
import com.elfennani.aniwatch.data_old.local.entities.LocalDownloadState
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.enums.ShowSeason
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.models.Tag
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.Date

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listType)

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

    // List<String>
    @TypeConverter
    fun fromListString(list: List<String>?): String? {
        return list?.let { adapter.toJson(list) }
    }

    @TypeConverter
    fun toListString(json: String?): List<String>? {
        return json?.let { adapter.fromJson(json) }
    }

    // ShowSeason
    @TypeConverter
    fun fromShowSeason(showSeason: ShowSeason?): String? {
        return showSeason?.name
    }

    @TypeConverter
    fun toShowSeason(name: String?): ShowSeason? {
        return name?.let { ShowSeason.valueOf(it) }
    }

    @TypeConverter
    fun fromEpisodeAudio(episodeAudio: EpisodeAudio) = episodeAudio.name

    @TypeConverter
    fun toEpisodeAudio(value: String) = EpisodeAudio.valueOf(value)


    @TypeConverter
    fun fromLocalDownloadState(downloadState: LocalDownloadState) = downloadState.name

    @TypeConverter
    fun toLocalDownloadState(value: String) = LocalDownloadState.valueOf(value)

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
        try{
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val adapter = moshi
                .adapter<List<Tag>>(
                    Types.newParameterizedType(List::class.java, Tag::class.java)
                )

            return adapter.fromJson(json)!!
        }catch (e:Exception){
            e.printStackTrace()
            return emptyList()
        }
    }
}