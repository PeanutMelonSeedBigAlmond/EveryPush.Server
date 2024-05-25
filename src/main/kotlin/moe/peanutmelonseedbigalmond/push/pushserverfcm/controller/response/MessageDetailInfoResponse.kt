package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType

data class MessageDetailInfoResponse(
    val id: Long,
    val uid: String,
    val title: String?,
    val excerpt: String,
    val type: MessageType,
    val coverUrl: String?,
    val encrypted: Boolean,
    val messageGroupId: String?,
    val pushedAt: Long,
)