package com.elfennani.aniwatch.data.remote.converters.enums


import com.elfennani.aniwatch.domain.models.enums.CharacterRole
import com.elfennani.anilist.type.CharacterRole as AniListCharacterRole

fun AniListCharacterRole.asAppModel() = when(this){
    AniListCharacterRole.MAIN -> CharacterRole.MAIN
    AniListCharacterRole.SUPPORTING -> CharacterRole.SUPPORTING
    AniListCharacterRole.BACKGROUND -> CharacterRole.BACKGROUND
    AniListCharacterRole.UNKNOWN__ -> throw Exception()
}