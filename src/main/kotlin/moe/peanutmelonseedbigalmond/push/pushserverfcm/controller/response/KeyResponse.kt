package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class KeyResponse(
    val id: Long,
    val name: String,
    val key: String,
    val createdAt: Long,
    val uid: String,
)