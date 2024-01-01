package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.typereslover

import graphql.TypeResolutionEnvironment
import graphql.schema.GraphQLObjectType
import graphql.schema.TypeResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicItem
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicItemWithCursor

class TopicItemTypeResolver : TypeResolver {
    override fun getType(env: TypeResolutionEnvironment): GraphQLObjectType {
        val obj = env.getObject<Any>()
        return when (obj) {
            is TopicItem -> env.schema.getObjectType("TopicItem")
            is TopicItemWithCursor -> env.schema.getObjectType("TopicItemWithCursor")
            else -> throw GraphqlException("unknown type:${obj.javaClass}")
        }
    }
}