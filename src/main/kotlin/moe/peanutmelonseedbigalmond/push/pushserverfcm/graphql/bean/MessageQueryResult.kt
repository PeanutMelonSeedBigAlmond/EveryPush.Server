package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class MessageQueryResult(
    val pageInfo: QueryPageInfo,
    val messages: List<MessageItemWithCursor>,
)

data class MessageItem(
    val owner: Long,
    val id: Long,
    val type: String,
    val text: String,
    val title: String?,
    val sendAt: Long,
)

data class MessageItemWithCursor(
    val owner: Long,
    val id: Long,
    val type: String,
    val text: String,
    val title: String?,
    val sendAt: Long,
    val cursor: String, // base64(cursor+偏移量, utf8)
)
