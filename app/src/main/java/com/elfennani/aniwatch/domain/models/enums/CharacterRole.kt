package com.elfennani.aniwatch.domain.models.enums

enum class CharacterRole {
    MAIN,
    SUPPORTING,
    BACKGROUND;

    companion object {
        val CharacterRole.value: String
            get() = this.name.lowercase().replaceFirstChar(Char::uppercase)
    }
}