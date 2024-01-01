package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class PushTokenItem(
    val id: Long,
    val token: String,
    val name: String,
    val generatedAt: Long,
)

data class PushTokenItemWithCursor(
    val id: Long,
    val token: String,
    val name: String,
    val generatedAt: Long,
    val cursor: String,
)

data class PushTokenQueryResult(
    val pageInfo: QueryPageInfo,
    val items: List<PushTokenItemWithCursor>,
)