package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class PingQuery : GraphQLQueryResolver {
    fun ping(): String {
        return "pong~"
    }
}