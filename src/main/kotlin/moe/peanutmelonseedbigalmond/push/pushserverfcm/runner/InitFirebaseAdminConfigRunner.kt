package moe.peanutmelonseedbigalmond.push.pushserverfcm.runner

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.File

@Component
@Order(1)
class InitFirebaseAdminConfigRunner : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val file = File("firebase-adminsdk.json")
        require(file.exists()) { "Firebase admin configuration not exists" }
        file.inputStream().use {
            FirebaseApp.initializeApp(
                FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(it)).build()
            )
        }
    }
}