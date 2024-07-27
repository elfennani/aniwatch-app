package com.elfennani.aniwatch.data.remote.models

import androidx.compose.ui.graphics.Color
import com.elfennani.aniwatch.models.ShowImage
import com.squareup.moshi.JsonClass

data class SerializableShowImage(
    val large: String,
    val medium: String,
    val original: String,
    val color: String?
)

fun SerializableShowImage.toDomain(): ShowImage {
    var color: Color? = null

    if(this.color != null){
        color = Color(android.graphics.Color.parseColor(this.color))
    }

    return ShowImage(
        large = this.large,
        medium = this.medium,
        original = this.original,
        color = color
    )
}