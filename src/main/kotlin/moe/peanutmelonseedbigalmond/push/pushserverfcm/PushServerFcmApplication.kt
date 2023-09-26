package moe.peanutmelonseedbigalmond.push.pushserverfcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
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
    init {
        val f = File("data")
        if (!f.exists()) {
            f.mkdirs()
        }

        FileInputStream("firebase-adminsdk.json").use {
            val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(it))
                .build()
            FirebaseApp.initializeApp(options)
        }
    }
//    @Bean
//    fun servletListener():ServletListenerRegistrationBean<ServletContextListener>{
//        val srb=ServletListenerRegistrationBean<ServletContextListener>()
//        srb.listener=object :ServletContextListener{
//            override fun contextDestroyed(sce: ServletContextEvent?) {
//                println("exited")
//            }
//        }
//        return srb
//    }

//    @EventListener
//    fun handleEvent(event:Any){
//        println(event)
//    }

    @EventListener
    fun handleContextClose(event: ContextClosedEvent) {
//        println(event)
//        Thread.sleep(5000)
    }

    @EventListener
    fun onApplicationStarted(event: ApplicationStartedEvent) {

    }

    @EventListener
    fun onApplicationReady(event: ApplicationReadyEvent) {

    }
}

fun main(args: Array<String>) {
    runApplication<PushServerFcmApplication>(*args)
}
