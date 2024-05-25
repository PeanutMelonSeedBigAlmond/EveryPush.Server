package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/serverInfo")
class ServerInfoController {
    @GetMapping("ping")
    fun ping(): String {
        return "pong"
    }
}