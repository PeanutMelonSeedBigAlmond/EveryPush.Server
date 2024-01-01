package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

import graphql.annotations.annotationTypes.GraphQLTypeResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.typereslover.TopicItemTypeResolver

@GraphQLTypeResolver(TopicItemTypeResolver::class)
abstract class BaseTopicItem {
    abstract val id: String?
    abstract val name: String
    abstract val owner: Long
}

data class TopicItem(
    override val id: String?,
    override val name: String,
    override val owner: Long,
) : BaseTopicItem()

data class TopicItemWithCursor(
    override val id: String?,
    override val name: String,
    override val owner: Long,
    val cursor: String,
) : BaseTopicItem()

data class TopicQueryResult(
    val pageInfo: QueryPageInfo,
    val items: List<BaseTopicItem>,
)