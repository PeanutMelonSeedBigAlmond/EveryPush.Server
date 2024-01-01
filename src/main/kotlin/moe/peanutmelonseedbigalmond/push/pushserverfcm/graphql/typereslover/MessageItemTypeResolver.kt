package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.typereslover

import graphql.TypeResolutionEnvironment
import graphql.schema.GraphQLObjectType
import graphql.schema.TypeResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessageItem
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessageItemWithCursor

class MessageItemTypeResolver : TypeResolver {
    override fun getType(env: TypeResolutionEnvironment): GraphQLObjectType {
        val obj = env.getObject<Any>()
        return when (obj) {
            is MessageItem -> env.schema.getObjectType("SingleMessageItem")
            is MessageItemWithCursor -> env.schema.getObjectType("MessageItemWithCursor")
            else -> throw RuntimeException("unknown type")
        }
    }
}