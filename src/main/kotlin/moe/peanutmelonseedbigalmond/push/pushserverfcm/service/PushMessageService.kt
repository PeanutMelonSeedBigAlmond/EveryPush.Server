package moe.peanutmelonseedbigalmond.push.pushserverfcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.FCMMessage
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.FCMPushData
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.FCMCommand
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.MarkdownUtil
import org.springframework.stereotype.Component

@Component
class PushMessageService {
    companion object {
        private val messagingService by lazy { FirebaseMessaging.getInstance() }
        private val gson = Gson()
    }

    suspend fun pushMessage(
        uid: String,
        title: String?,
        content: String,
        type: MessageType,
        priority: Int,
        coverImgUrl: String?,
        encrypted: Boolean,
        messageGroupId: String?,
        messageGroupTitle: String?,
        id: Long,
        deviceTokens: List<String>,
    ): Int {
        val slicedDevices = deviceTokens.chunked(500)
        val excerpt = when (type) {
            MessageType.Text -> content.take(40)
            MessageType.Picture -> content
            MessageType.Markdown -> MarkdownUtil.getMarkdownExcerpt(content, 40)
        }

        val jobs = coroutineScope {
            slicedDevices.map {
                async {
                    val message = MulticastMessage.builder()
                        .putData(
                            "data",
                            getMessageBody(
                                uid,
                                title,
                                excerpt,
                                type,
                                priority,
                                coverImgUrl,
                                encrypted,
                                messageGroupId,
                                messageGroupTitle,
                                id
                            )
                        )
                        .addAllTokens(deviceTokens)
                        .build()
                    messagingService.sendEachForMulticast(message)
                }
            }
        }

        val result = jobs.awaitAll()
        val successCount = result.sumOf { it.successCount }
        return successCount
    }

    private fun getMessageBody(
        uid: String,
        title: String?,
        excerpt: String,
        type: MessageType,
        priority: Int,
        coverImgUrl: String?,
        encrypted: Boolean,
        messageGroupId: String?,
        messageGroupTitle: String?,
        id: Long,
    ): String {
        val bean = FCMPushData(
            FCMCommand.PushMessage,
            FCMMessage(
                uid,
                title,
                excerpt,
                type,
                priority,
                coverImgUrl,
                encrypted,
                messageGroupId,
                messageGroupTitle,
                id
            )
        )
        return gson.toJson(bean)
    }
}