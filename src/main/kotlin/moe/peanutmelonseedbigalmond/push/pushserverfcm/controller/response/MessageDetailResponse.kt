package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType

data class MessageDetailResponse(
    val id: Long,
    val title: String?,
    val content: String,
    val coverImgUrl: String?,
    val type: MessageType,
    val pushedAt: Long,
    val uid: String,
    val messageGroupId: String?,
    val encrypted: Boolean,
)