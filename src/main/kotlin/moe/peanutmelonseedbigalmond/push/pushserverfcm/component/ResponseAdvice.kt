package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import com.fasterxml.jackson.databind.ObjectMapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ResponseAdvice : ResponseBodyAdvice<Any> {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean =
        true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return when (body) {
            is ResponseWrapper<*> -> body

            is String -> {
                response.headers.add("Content-type", "application/json; charset=UTF-8")
                objectMapper.writeValueAsString(ResponseWrapper.successWith(body))
            }

            is Unit -> ResponseWrapper.successWith(null)

            else -> ResponseWrapper.successWith(body)
        }
    }
}