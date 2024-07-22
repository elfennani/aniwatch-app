package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.ShowSeason
import com.elfennani.aniwatch.models.ShowStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class SerializableShowStatus(private val value:String){
    @Json(name = "watching")
    WATCHING("watching"),

    @Json(name = "on_hold")
    ON_HOLD("on_hold"),

    @Json(name = "completed")
    COMPLETED("completed"),

    @Json(name = "dropped")
    DROPPED("dropped"),

    @Json(name = "plan_to_watch")
    PLAN_TO_WATCH("plan_to_watch"),

    @Json(name = "repeating")
    REPEATING("repeating");

    override fun toString(): String {
        return value
    }
}

fun SerializableShowStatus.toDomain() = ShowStatus.valueOf(this.name)
fun ShowStatus.toSerializable() = SerializableShowStatus.valueOf(this.name)