package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class QueryPageInfo(
    val firstCursor: String?,
    val lastCursor: String?,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val total: Int,
    val count: Int,
)