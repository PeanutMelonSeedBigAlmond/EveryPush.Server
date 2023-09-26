package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class FetchPushTokenResponse(
    val id: Long,
    val token: String,
    val name: String,
    val generatedAt:Long,
)