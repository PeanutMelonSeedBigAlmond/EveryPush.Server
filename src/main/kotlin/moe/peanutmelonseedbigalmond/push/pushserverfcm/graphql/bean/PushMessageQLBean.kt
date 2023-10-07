package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class PushMessageQLBean(
    val failedCount: Int,
    val messageId: Long,
    val pushedAt: Long,
)