package moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.SendMessageResult

object FCMMessageUtil {
    private val allowedNotificationType = arrayOf("text", "image", "markdown")

    @JvmStatic
    fun sendNotification(
        fcmTokens: List<String>,
        text: String,
        title: String = "",
        type: String = "text",
        topic: String = "",
    ): List<SendMessageResult> {
        val message = MulticastMessage.builder()
            .setCommand(FCMMessageCommand.Notification)
            .setNotificationTitle(title)
            .setNotificationType(type.takeIf { it in allowedNotificationType } ?: "text")
            .setNotificationBody(text)
            .setTopic(topic)
            .addAllTokens(fcmTokens.take(500))
            .build()
        val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
        val responseBody = response.responses
        val sendMessageResultList = responseBody.mapIndexed { index, element ->
            return@mapIndexed SendMessageResult(fcmTokens[index], element.isSuccessful)
        }

        return sendMessageResultList
    }

    @JvmStatic
    private fun MulticastMessage.Builder.setCommand(fcmMessageCommand: FCMMessageCommand): MulticastMessage.Builder {
        return apply {
            putData("command", fcmMessageCommand.command.toString())
        }
    }

    @JvmStatic
    private fun MulticastMessage.Builder.setNotificationBody(body: String): MulticastMessage.Builder {
        return apply {
            putData("text", body)
        }
    }

    @JvmStatic
    private fun MulticastMessage.Builder.setNotificationTitle(title: String): MulticastMessage.Builder {
        return apply {
            putData("title", title)
        }
    }

    @JvmStatic
    private fun MulticastMessage.Builder.setNotificationType(type: String): MulticastMessage.Builder {
        return apply {
            putData("type", type)
        }
    }

    @JvmStatic
    private fun MulticastMessage.Builder.setTopic(topicId: String): MulticastMessage.Builder {
        return apply {
            putData("topic", topicId)
        }
    }
}