package moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm

import com.google.firebase.messaging.AndroidConfig
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
        topic: String? = null,
        topicName: String? = null
    ): List<SendMessageResult> {
        val tokenList = fcmTokens.take(500)
        val message = MulticastMessage.builder()
            .setCommand(FCMMessageCommand.Notification)
            .setNotificationTitle(title)
            .setNotificationType(type.takeIf { it in allowedNotificationType } ?: "text")
            .setNotificationBody(text)
            .apply { setTopic(topic, topicName) }
            .addAllTokens(tokenList)
            .setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
            .build()
        return doSendMulticastMessage(tokenList, message)
    }

    @JvmStatic
    fun notifyTopicAdded(
        fcmTokens: List<String>,
        topic: String,
        topicName: String
    ): List<SendMessageResult> {
        val tokenList = fcmTokens.take(500)
        val message = MulticastMessage.builder()
            .setCommand(FCMMessageCommand.AddTopic)
            .setTopic(topic, topicName)
            .addAllTokens(tokenList)
            .build()
        return doSendMulticastMessage(tokenList, message)
    }

    fun notifyTopicDeleted(
        fcmTokens: List<String>,
        topic: String,
    ): List<SendMessageResult> {
        val tokenList = fcmTokens.take(500)
        val message = MulticastMessage.builder()
            .setCommand(FCMMessageCommand.DeleteTopic)
            .setTopic(topic, null)
            .addAllTokens(tokenList)
            .build()
        return doSendMulticastMessage(tokenList, message)
    }

    @JvmStatic
    fun doSendMulticastMessage(tokenList: List<String>, message: MulticastMessage): List<SendMessageResult> {
        val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
        val responseBody = response.responses
        val sendMessageResultList = responseBody.mapIndexed { index, element ->
            return@mapIndexed SendMessageResult(tokenList[index], element.isSuccessful)
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
    private fun MulticastMessage.Builder.setTopic(topicId: String?, topicName: String?): MulticastMessage.Builder {
        return apply {
            if (topicId != null) {
                putData("topic", topicId)
            }
            if (topicName != null) {
                putData("topicName", topicName)
            }
        }
    }
}