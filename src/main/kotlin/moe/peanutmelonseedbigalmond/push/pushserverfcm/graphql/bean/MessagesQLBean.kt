package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class MessagesQLBean(
    val owner: Long,
    val id: Long,
    val type: String,
    val text: String,
    val title: String?,
    val sendAt: Long,
    val topicId: String?
)