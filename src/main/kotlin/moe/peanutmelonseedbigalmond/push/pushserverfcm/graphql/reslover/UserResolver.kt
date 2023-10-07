package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserResolver : GraphQLResolver<UserQLBean> {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    fun getDevices(bean: UserQLBean): List<DeviceQLBean> {
        return deviceRepository.getDeviceInfosByOwner(bean.uid).map {
            return@map DeviceQLBean(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }

    fun getTopics(bean: UserQLBean): List<TopicQLBean> {
        return topicRepository.findByPk_Owner(bean.uid).map {
            return@map TopicQLBean(it.pk.topicId, it.name)
        }
    }

    fun getMessages(bean: UserQLBean): List<MessagesQLBean> {
        return messageRepository.findByOwnerAndNotDeleted(bean.uid).map {
            return@map MessagesQLBean(it.owner, it.messageId, it.type, it.text, it.title, it.pushTime, it.topicId)
        }
    }

    fun getTokens(bean: UserQLBean): List<PushTokenQLBean> {
        return pushTokenRepository.getPushTokenInfosByOwner(bean.uid).map {
            return@map PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        }
    }
}