package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import graphql.GraphQLError

class GraphqlErrorAdapter(val message: String) {
    companion object {
        fun fromGraphqlError(error: GraphQLError): GraphqlErrorAdapter {
            return GraphqlErrorAdapter(error.message)
        }
    }
}