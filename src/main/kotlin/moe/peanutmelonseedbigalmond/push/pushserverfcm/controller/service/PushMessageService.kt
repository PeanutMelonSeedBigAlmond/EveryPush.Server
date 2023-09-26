package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.gson.Gson
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.SendMessageResult
import org.springframework.stereotype.Component

@Component
class PushMessageService {
    private val gson = Gson()
    fun pushMessage(fcmTokenList: List<String>, data: Map<String, Any>): List<SendMessageResult> {
        val messageBuilder = MulticastMessage.builder()

        data.forEach { (k, v) ->
            if (v is String) {
                messageBuilder.putData(k, v)
            } else {
                messageBuilder.putData(k, gson.toJson(v))
            }
        }

        val message = messageBuilder.addAllTokens(fcmTokenList)
            .build()
        val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
        val responseBody = response.responses
        val sendMessageResultList = responseBody.mapIndexed { index, element ->
            return@mapIndexed SendMessageResult(fcmTokenList[index], element.isSuccessful)
        }


        return sendMessageResultList
    }
}