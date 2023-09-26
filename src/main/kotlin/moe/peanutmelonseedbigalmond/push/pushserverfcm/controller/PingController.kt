package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    /**
     * 监测服务是否可用
     * @return ResponseEntity<ResponseWrapper<Unit>>
     */
    @RequestMapping("/ping")
    suspend fun login(apiKey: String): ResponseEntity<ResponseWrapper<Unit>> {
        return ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
    }
}