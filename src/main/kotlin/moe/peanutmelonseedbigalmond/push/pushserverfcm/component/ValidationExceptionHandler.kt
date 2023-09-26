package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.util.logging.Logger

@ControllerAdvice
class ValidationExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = [Exception::class])
    fun argumentExceptionHandler(exception: Exception): ResponseEntity<ResponseWrapper<Unit>> {
        Logger.getLogger(this::class.java.name).warning(exception.cause?.localizedMessage ?: exception.localizedMessage ?: exception.toString())
        Logger.getLogger(this::class.java.name).warning(exception.stackTraceToString())
        return ResponseEntity(
            ResponseWrapper(
                message = exception.cause?.localizedMessage ?: exception.localizedMessage ?: exception.toString(),
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}