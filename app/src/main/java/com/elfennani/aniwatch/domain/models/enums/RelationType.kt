package com.elfennani.aniwatch.domain.models.enums

enum class RelationType {
    ADAPTATION,
    PREQUEL,
    SEQUEL,
    PARENT,
    SIDE_STORY,
    CHARACTER,
    SUMMARY,
    ALTERNATIVE,
    SPIN_OFF,
    OTHER,
    SOURCE,
    COMPILATION,
    CONTAINS;

    companion object {
        val RelationType.value: String
            get() = this.name
                .split("_")
                .joinToString(" ") {
                    it.lowercase().replaceFirstChar(Char::uppercase)
                }
    }
}