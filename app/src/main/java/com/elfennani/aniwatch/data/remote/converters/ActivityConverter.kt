package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.fragment.ListActivityFragment
import com.elfennani.anilist.fragment.TextActivityFragment
import com.elfennani.aniwatch.data.local.models.LocalActivity
import com.elfennani.aniwatch.domain.models.enums.ActivityType
import kotlinx.datetime.Instant


fun ListActivityFragment.asEntity(): LocalActivity {
    val content = mutableListOf<String>()

    if(!status.isNullOrEmpty()){
        content.add(status)
    }

    if(!progress.isNullOrEmpty())
        content.add(progress)

    val title =media?.showFragment?.title?.userPreferred
    if(!title.isNullOrEmpty())
        content.add(title)

    return LocalActivity(
        id = id,
        content = content.joinToString(" "),
        type = ActivityType.LIST,
        userId = user!!.userFragment.id,
        showId = media?.showFragment?.id,
        createdAt = Instant.fromEpochSeconds(createdAt.toLong()),
        likes = likeCount,
        replies = replyCount
    )
}

fun TextActivityFragment.asEntity() = LocalActivity(
    id = id,
    content = text!!,
    type = ActivityType.TEXT,
    userId = user!!.userFragment.id,
    showId = null,
    createdAt = Instant.fromEpochSeconds(createdAt.toLong()),
    likes = likeCount,
    replies = replyCount
)