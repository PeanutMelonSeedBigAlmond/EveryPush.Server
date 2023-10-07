package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushMessageParams
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushMessageQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm.FCMMessageUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.logging.Logger
import javax.validation.Valid

@Component
@Validated
class PushMessageMutation : GraphQLMutationResolver {
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
    fun pushMessage(
        @Valid params: PushMessageParams
    ): PushMessageQLBean {
        val tokenInfo = pushTokenRepository.getPushTokenInfoByPushToken(params.pushToken)
            ?: throw GraphqlException("push token does not exists")
        val deviceList = deviceRepository.getDeviceInfosByOwner(tokenInfo.owner)
        val fcmTokenList = deviceList.map { it.fcmToken }

        val tid = if (params.topicId != null && topicRepository.existsByPk(
                TopicInfo.TopicInfoPK(
                    owner = tokenInfo.owner,
                    topicId = params.topicId
                )
            )
        ) {
            params.topicId
        } else {
            logger.info("${tokenInfo.owner} 尝试推送到一个不存在的渠道 ${params.topicId}")
            null
        }


        if (fcmTokenList.isNotEmpty()) {
            val pushResult = FCMMessageUtil.sendNotification(
                fcmTokens = fcmTokenList,
                title = params.title!!,
                text = params.text,
                type = params.type!!,
                topic = tid
            )

            val message = MessageBean()
            message.text = params.text
            message.title = params.title!!
            message.type = params.type!!
            message.pushTime = System.currentTimeMillis()
            message.owner = tokenInfo.owner
            message.topicId = tid
            message.deleted = false

            val savedMessage = messageRepository.save(message)

            logger.info("${tokenInfo.owner} 推送消息成功，id=${savedMessage.messageId}")

            val failedCount = pushResult.count { !it.success }
            return savedMessage.let {
                return@let PushMessageQLBean(failedCount, savedMessage.messageId, savedMessage.pushTime)
            }
        } else {
            logger.warning("${tokenInfo.owner} 没有已经注册的设备")
            throw GraphqlException("No device registered")
        }
    }
}