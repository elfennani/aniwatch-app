package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.type.MediaRelation
import com.elfennani.aniwatch.domain.models.enums.RelationType

fun MediaRelation.asAppModel() = when(this){
    MediaRelation.ADAPTATION -> RelationType.ADAPTATION
    MediaRelation.PREQUEL -> RelationType.PREQUEL
    MediaRelation.SEQUEL -> RelationType.SEQUEL
    MediaRelation.PARENT -> RelationType.PARENT
    MediaRelation.SIDE_STORY -> RelationType.SIDE_STORY
    MediaRelation.CHARACTER -> RelationType.CHARACTER
    MediaRelation.SUMMARY -> RelationType.SUMMARY
    MediaRelation.ALTERNATIVE -> RelationType.ALTERNATIVE
    MediaRelation.SPIN_OFF -> RelationType.SPIN_OFF
    MediaRelation.OTHER -> RelationType.OTHER
    MediaRelation.SOURCE -> RelationType.SOURCE
    MediaRelation.COMPILATION -> RelationType.COMPILATION
    MediaRelation.CONTAINS -> RelationType.CONTAINS
    MediaRelation.UNKNOWN__ -> null
}