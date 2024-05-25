package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import jakarta.validation.ValidationException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.ApiException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(e: Exception): ResponseWrapper<Any?> {
        e.printStackTrace()
        return when (e) {
            is ValidationException -> ResponseWrapper.failed(ResponseErrorCode.MalformedParameters, e.localizedMessage)
            is ApiException -> ResponseWrapper.failed(e.errorCode, e.errorMessage ?: e.localizedMessage)
            else -> ResponseWrapper.failed(ResponseErrorCode.InternalError, e.localizedMessage)
        }
    }
}