package com.elfennani.aniwatch.domain.models

data class Tag(
    val id: Int,
    val label: String,
    val percentage: Int,
    val spoiler: Boolean,
)


//export interface Tag {
//    id: number
//    label: string
//    percentage: number
//    spoiler: boolean
//}
