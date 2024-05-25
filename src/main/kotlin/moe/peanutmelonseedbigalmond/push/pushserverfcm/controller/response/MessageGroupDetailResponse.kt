package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class MessageGroupDetailResponse(
    val groupId: String?,
    val id: Long?,
    val name: String?,
    val messageCount: Int,
)