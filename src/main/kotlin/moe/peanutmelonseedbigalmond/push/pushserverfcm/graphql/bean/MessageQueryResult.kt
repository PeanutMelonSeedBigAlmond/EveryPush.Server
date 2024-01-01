package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

import graphql.annotations.annotationTypes.GraphQLTypeResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.typereslover.MessageItemTypeResolver

@GraphQLTypeResolver(MessageItemTypeResolver::class)
data class MessageQueryResult(
    val pageInfo: QueryPageInfo,
    val messages: List<BaseMessageItem>,
)

abstract class BaseMessageItem {
    abstract val owner: Long
    abstract val id: Long
    abstract val type: String
    abstract val text: String
    abstract val title: String?
    abstract val sendAt: Long
}

data class MessageItem(
    override val owner: Long,
    override val id: Long,
    override val type: String,
    override val text: String,
    override val title: String?,
    override val sendAt: Long,
) : BaseMessageItem()

data class MessageItemWithCursor(
    override val owner: Long,
    override val id: Long,
    override val type: String,
    override val text: String,
    override val title: String?,
    override val sendAt: Long,
    val cursor: String,
) : BaseMessageItem()
