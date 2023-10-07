package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean


data class LatestMessagesQLBean(
    val id: Long,
    val type: String,
    val text: String,
    val title: String?,
    val sendAt: Long,
)