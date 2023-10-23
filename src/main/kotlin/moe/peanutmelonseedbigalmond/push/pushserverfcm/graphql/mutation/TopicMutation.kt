package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm.FCMMessageUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.logging.Logger
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Component
@Validated
class TopicMutation : GraphQLMutationResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    fun createTopic(
        @Size(max = 20)
        @Pattern(regexp = "^[0-9a-zA-Z-_.]+$")
        id: String,
        @NotBlank @Size(max = 40) name: String,
        @NotBlank token: String
    ): TopicQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val pk = TopicInfo.TopicInfoPK()
        pk.topicId = id
        pk.owner = uid

        checkThrowGraphqlException(!topicRepository.existsByPk(pk)) { "Topic already exists" }

        val fcmTokenList = deviceRepository.getDeviceInfosByOwner(uid)
            .map { it.fcmToken }

        val topic = TopicInfo()
        topic.name = name
        topic.pk = pk

        try {
            FCMMessageUtil.notifyTopicAdded(fcmTokenList, topic.pk.topicId, topic.name)
        } catch (e: Exception) {
            Logger.getLogger(this::class.simpleName)
                .warning("通知客户端失败: createTopic")
            e.printStackTrace()
        }

        return topicRepository.save(topic).let {
            return@let TopicQLBean(it.pk.topicId, it.name, uid)
        }
    }

    @Transactional
    fun deleteTopic(@NotBlank id: String, @NotBlank token: String): List<TopicQLBean> {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val fcmTokenList = deviceRepository.getDeviceInfosByOwner(uid)
            .map { it.fcmToken }
        val res = topicRepository.deleteByPk(TopicInfo.TopicInfoPK(uid, id)).also {
            checkThrowGraphqlException(it.isNotEmpty()) { "Topic does not exists" }
        }.map {
            return@map TopicQLBean(it.pk.topicId, it.name, uid)
        }.also {
            for (item in it) {
                messageRepository.moveTopicAllMessageToDefaultTopic(item.id!!, item.owner)
            }
        }

        res.forEach {
            try {
                FCMMessageUtil.notifyTopicDeleted(fcmTokenList, it.id!!)
            } catch (e: Exception) {
                Logger.getLogger(this::class.simpleName)
                    .warning("通知客户端失败: deleteTopic")
                e.printStackTrace()
            }
        }

        return res
    }

    private inline fun checkThrowGraphqlException(value: Boolean, message: () -> String) {
        if (!value) {
            throw GraphqlException(message())
        }
    }
}