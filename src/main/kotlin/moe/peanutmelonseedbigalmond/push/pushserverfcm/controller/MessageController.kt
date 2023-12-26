package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueList
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushMessageQLBean
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
import javax.validation.constraints.Pattern

@RestController
@RequestMapping("/message")
@Validated
class MessageController {
    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    private val logger = Logger.getLogger(this::class.java.name)

    /**
     * 推送消息
     */
    @RequestMapping("/push")
    suspend fun pushMessage(
        @NotEmpty pushToken: String,
        @NotEmpty text: String,
        @RequestParam(defaultValue = "") title: String,
        @RequestParam(defaultValue = "text") @ValueList(values = ["text", "image", "markdown"]) type: String,
        topicId: String?,
        @Pattern(regexp = "https?://.*", message = "Malformed url") originalUrl: String?,
    ): ResponseEntity<RestApiResponseWrapper<PushMessageQLBean>> {
        val tokenInfo = pushTokenRepository.getPushTokenInfoByPushToken(pushToken) ?: return ResponseEntity(
            RestApiResponseWrapper(message = "Push token does not exists"), HttpStatus.BAD_REQUEST
        )
        val deviceList = deviceRepository.getDeviceInfosByOwner(tokenInfo.owner)
        val fcmTokenList = deviceList.map { it.fcmToken }

        val topic = if (topicId != null) {
            val t = topicRepository.findByPk(TopicInfo.TopicInfoPK(tokenInfo.owner, topicId))
            if (t != null) {
                t
            } else {
                logger.info("${tokenInfo.owner} 尝试推送到一个不存在的渠道 $topicId")
                null
            }
        } else {
            null
        }

        if (fcmTokenList.isNotEmpty()) {
            val pushResult = FCMMessageUtil.sendNotification(
                fcmTokens = fcmTokenList,
                title = title,
                text = text,
                type = type,
                topic = topic?.pk?.topicId,
                topicName = topic?.name,
                originalUrl = originalUrl
            )

            val message = MessageBean()
            message.text = text
            message.title = title
            message.type = type
            message.pushTime = System.currentTimeMillis()
            message.owner = tokenInfo.owner
            message.topicId = topic?.pk?.topicId
            message.deleted = false
            message.originalUrl = originalUrl

            val savedMessage = messageRepository.save(message)

            logger.info("${tokenInfo.owner} 推送消息成功，id=${savedMessage.messageId}")

            val failedCount = pushResult.count { !it.success }
            return ResponseEntity(RestApiResponseWrapper(data = savedMessage.let {
                return@let PushMessageQLBean(
                    failedCount,
                    savedMessage.messageId,
                    savedMessage.pushTime
                )
            }), HttpStatus.OK)
        } else {
            logger.warning("${tokenInfo.owner} 没有已经注册的设备")
            return ResponseEntity(RestApiResponseWrapper(message = "No device registered"), HttpStatus.BAD_REQUEST)
        }
    }
}