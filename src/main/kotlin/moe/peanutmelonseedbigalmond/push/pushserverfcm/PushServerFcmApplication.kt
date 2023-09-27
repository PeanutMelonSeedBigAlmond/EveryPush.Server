package moe.peanutmelonseedbigalmond.push.pushserverfcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import moe.peanutmelonseedbigalmond.push.pushserverfcm.util.ClearExpiredToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.io.File
import java.io.FileInputStream

@SpringBootApplication
@EnableJpaAuditing
class PushServerFcmApplication {
    @Autowired
    lateinit var clearExpiredToken: ClearExpiredToken

    init {
        val f = File("data")
        if (!f.exists()) {
            f.mkdirs()
        }

        FileInputStream("firebase-adminsdk.json").use {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(it))
                .build()
            FirebaseApp.initializeApp(options)
        }
    }

//    @EventListener
//    fun handleEvent(event:Any){
//        println(event)
//    }

    @EventListener
    fun handleContextClose(event: ContextClosedEvent) {
        clearExpiredToken.stop()
    }

    @EventListener
    fun onApplicationStarted(event: ApplicationStartedEvent) {

    }

    @EventListener
    fun onApplicationReady(event: ApplicationReadyEvent) {
        clearExpiredToken.start()
    }
}

fun main(args: Array<String>) {
    runApplication<PushServerFcmApplication>(*args)
}
