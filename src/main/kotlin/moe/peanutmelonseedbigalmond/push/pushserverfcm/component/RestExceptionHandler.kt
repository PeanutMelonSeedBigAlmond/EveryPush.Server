package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.RestApiResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice("moe.peanutmelonseedbigalmond.push.pushserverfcm.controller")
@Component
class RestExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun handler(e: Exception): ResponseEntity<RestApiResponseWrapper<Unit?>> {
        return ResponseEntity(
            RestApiResponseWrapper(message = e.message ?: e.toString()),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}