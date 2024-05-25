package moe.peanutmelonseedbigalmond.push.pushserverfcm.data

import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType

data class FCMMessage(
    val uid: String,
    val title: String?,
    val excerpt: String,
    val type: MessageType,
    val priority: Int,
    val coverImgUrl: String?,
    val encrypted: Boolean,
    val messageGroupId: String?,
    val messageGroupTitle: String?,
    val id: Long,
)