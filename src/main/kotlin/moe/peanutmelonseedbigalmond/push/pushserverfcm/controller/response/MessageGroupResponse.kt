package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class MessageGroupResponse(
    val id: Long,
    val uid: String,
    val groupId: String,
    val name: String,
    val createdAt: Long,
)