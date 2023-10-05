package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import kotlinx.coroutines.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueList
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.FetchMessageResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.PushMessageResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm.FCMMessageUtil
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

    private val logger = Logger.getLogger(this::class.java.name)

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
        @RequestParam(defaultValue = "text") @ValueList(values = ["text", "image", "markdown"]) type: String
    ): ResponseEntity<ResponseWrapper<PushMessageResponse>> {
        val tokenInfo = pushTokenRepository.getPushTokenInfoByPushToken(pushToken) ?: return ResponseEntity(
            ResponseWrapper(
                message = "token 不存在"
            ), HttpStatus.BAD_REQUEST
        )
        val deviceList = deviceRepository.getDeviceInfosByOwner(tokenInfo.owner)
        val fcmTokenList = deviceList.map { it.fcmToken }

        if (fcmTokenList.isNotEmpty()) {
            val pushResult = FCMMessageUtil.sendNotification(
                fcmTokens = fcmTokenList,
                title = title,
                text = text,
                type = type
            )

            val message = MessageBean()
            message.text = text
            message.title = title
            message.type = type
            message.pushTime = System.currentTimeMillis()
            message.owner = tokenInfo.owner
            message.deleted = false

            val savedMessage = messageRepository.save(message)

            logger.info("${tokenInfo.owner} 推送消息成功，id=${savedMessage.messageId}")
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
            logger.warning("${tokenInfo.owner} 没有已经注册的设备")
            return ResponseEntity(
                ResponseWrapper(data = PushMessageResponse(0, 0, System.currentTimeMillis())),
                HttpStatus.OK
            )
        }
    }

    /**
     * 获取所有未被删除消息记录
     * @param uid Long
     * @return ResponseEntity<ResponseWrapper<List<FetchMessageResponse>>>
     */
    @RequestMapping("/all")
    suspend fun fetchAllMessages(@NotNull uid: Long): ResponseEntity<ResponseWrapper<List<FetchMessageResponse>>> {
        val messages = messageRepository.findByOwnerAndDeleted(owner = uid, deleted = false).map {
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
        val messageCount =
            messageRepository.updateDeletedByOwnerAndMessageId(owner = uid, messageId = id, deleted = true)

        logger.info("用户 $uid 删除了 $messageCount 条消息")
        return ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
    }

    @RequestMapping("/clear")
    suspend fun clearMessage(@NotNull uid: Long): ResponseEntity<ResponseWrapper<Unit>> {
        val deleteCount = messageRepository.updateDeletedByOwner(deleted = true, owner = uid)
        logger.info("$uid 删除了 $deleteCount 消息记录")
        return ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
    }
}