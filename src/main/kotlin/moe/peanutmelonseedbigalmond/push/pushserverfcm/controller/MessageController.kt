package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import kotlinx.coroutines.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.FetchMessageResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.PushMessageResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.service.PushMessageService
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/message")
@Validated
class MessageController {
    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var messageService: PushMessageService

    /**
     * 推送消息
     * @param pushToken String
     * @param text String
     * @param title String
     * @param type String
     * @return ResponseEntity<ResponseWrapper<PushMessageResponse>>
     */
    @RequestMapping("/push")
    suspend fun pushMessage(
        @NotEmpty pushToken: String,
        @NotEmpty text: String,
        @RequestParam(defaultValue = "") title: String,
        @RequestParam(defaultValue = "text") type: String
    ): ResponseEntity<ResponseWrapper<PushMessageResponse>> {
        val tokenInfo = pushTokenRepository.getPushTokenInfoByPushToken(pushToken) ?: return ResponseEntity(
            ResponseWrapper(
                message = "token 不存在"
            ), HttpStatus.BAD_REQUEST
        )
        val deviceList = deviceRepository.getDeviceInfosByOwner(tokenInfo.owner)
        val fcmTokenList = deviceList.map { it.fcmToken }

        if (fcmTokenList.isNotEmpty()) {
            val pushResult = messageService.pushMessage(
                fcmTokenList, mapOf(
                    "text" to text,
                    "type" to type,
                    "title" to title
                )
            )

            val message = MessageBean()
            message.text = text
            message.title = title
            message.type = type
            message.pushTime = System.currentTimeMillis()
            message.owner = tokenInfo.owner

            val savedMessage = messageRepository.save(message)

            Logger.getLogger(this::class.java.name).info("推送消息成功，id=${savedMessage.messageId}")
            return ResponseEntity(
                ResponseWrapper(
                    data = PushMessageResponse(
                        pushResult.count { !it.success },
                        savedMessage.messageId,
                        savedMessage.pushTime
                    )
                ),
                HttpStatus.OK
            )
        } else {
            Logger.getLogger(this::class.java.name).warning("没有已经注册的设备")
            return ResponseEntity(
                ResponseWrapper(data = PushMessageResponse(0, 0, System.currentTimeMillis())),
                HttpStatus.OK
            )
        }
    }

    /**
     * 获取所有消息记录
     * @param uid Long
     * @return ResponseEntity<ResponseWrapper<List<FetchMessageResponse>>>
     */
    @RequestMapping("/all")
    suspend fun fetchAllMessages(@NotNull uid: Long): ResponseEntity<ResponseWrapper<List<FetchMessageResponse>>> {
        val messages = messageRepository.getMessageBeansByOwner(uid).map {
            return@map FetchMessageResponse(it.messageId, it.text, it.title, it.type, it.pushTime)
        }
        return ResponseEntity(ResponseWrapper(data = messages), HttpStatus.OK)
    }

    /**
     * 删除指定的消息记录
     * @param uid Long
     * @param id Long
     * @return ResponseEntity<ResponseWrapper<Unit>>
     */
    @RequestMapping("/remove")
    suspend fun removeMessage(@NotNull uid: Long, @NotNull id: Long): ResponseEntity<ResponseWrapper<Unit>> {
        val message = messageRepository.getMessageBeanByMessageId(id)
        return if (message == null) {
            ResponseEntity(
                ResponseWrapper(message = "指定的消息不存在"),
                HttpStatus.BAD_REQUEST
            )
        } else if (message.owner != uid) {
            ResponseEntity(
                ResponseWrapper(message = "非法操作"),
                HttpStatus.FORBIDDEN
            )
        } else {
            messageRepository.delete(message)
            return ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
        }
    }

    @RequestMapping("/clear")
    suspend fun clearMessage(@NotNull uid: Long): ResponseEntity<ResponseWrapper<Unit>> {
        messageRepository.deleteMessageBeansByOwner(uid)

        return ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
    }
}