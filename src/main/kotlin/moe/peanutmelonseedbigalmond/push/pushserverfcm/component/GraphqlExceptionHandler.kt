package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Component
@ControllerAdvice("moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql")
class GraphqlExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun graphqlExceptionHandler(e: Exception): GraphQLError {
        return GraphqlErrorBuilder.newError()
            .message(e.message ?: e.toString())
            .build()
    }
}