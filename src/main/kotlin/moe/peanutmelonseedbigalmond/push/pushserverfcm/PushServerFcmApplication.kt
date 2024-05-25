package moe.peanutmelonseedbigalmond.push.pushserverfcm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PushServerFcmApplication

fun main(args: Array<String>) {
    runApplication<PushServerFcmApplication>(*args)
}
