package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class PushMessageResponse(
    val failedCount: Int,
    val messageId:Long,
    val pushedAt:Long,
)