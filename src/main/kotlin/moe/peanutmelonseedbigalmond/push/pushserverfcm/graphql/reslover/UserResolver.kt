package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.OffsetBasedPageRequest
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.GraphqlCursor
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Min

@Validated
@Component
class UserResolver : GraphQLResolver<UserQLBean> {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var messageRepositoryWrapper: MessageRepositoryWrapper

    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    private val base64Decoder by lazy { Base64.getDecoder() }
    private val base64Encoder by lazy { Base64.getEncoder() }

    fun getDevices(bean: UserQLBean, @Min(1) count: Int, @GraphqlCursor after: String?): DeviceQueryResult {
        val totalCount = deviceRepository.countByOwner(bean.uid)
        val offset =
            if (after.isNullOrBlank()) 0 else base64Decoder.decode(after).toString(Charsets.UTF_8).substring(6)
                .toInt() + 1
        val devices = deviceRepository.getDeviceInfosByOwner(
            bean.uid,
            OffsetBasedPageRequest(offset, count)
        ).mapIndexed { index, item ->
            val indexInDatabase = index + offset
            val cursor = base64Encoder.encodeToString("cursor$indexInDatabase".toByteArray(Charsets.UTF_8))
            DeviceItemWithCursor(item.id, item.deviceType, item.fcmToken, item.name, cursor)
        }
        return DeviceQueryResult(
            QueryPageInfo(
                devices.firstOrNull()?.cursor,
                devices.firstOrNull()?.cursor,
                offset != 0,
                offset + devices.size != totalCount,
                totalCount,
                devices.size
            ),
            devices
        )
    }


    fun getTopics(bean: UserQLBean): List<TopicQLBean> {
        return topicRepository.findByPk_Owner(bean.uid).map {
            return@map TopicQLBean(it.pk.topicId, it.name, bean.uid)
        }
    }

    fun getMessages(bean: UserQLBean, @Min(1) count: Int, @GraphqlCursor after: String?): MessageQueryResult {
        val totalCount = messageRepositoryWrapper.countByOwnerAndDeleted(bean.uid, false)
        val offset =
            if (after.isNullOrBlank()) 0 else base64Decoder.decode(after).toString(Charsets.UTF_8).substring(6)
                .toInt() + 1
        val messages = messageRepositoryWrapper.findByOwnerAndNotDeleted(
            bean.uid,
            OffsetBasedPageRequest(offset, count)
        ).mapIndexed { index, item ->
            val indexInDatabase = index + offset
            val cursor = base64Encoder.encodeToString("cursor$indexInDatabase".toByteArray(Charsets.UTF_8))
            MessageItemWithCursor(bean.uid, item.messageId, item.type, item.text, item.title, item.pushTime, cursor)
        }
        return MessageQueryResult(
            QueryPageInfo(
                messages.firstOrNull()?.cursor,
                messages.firstOrNull()?.cursor,
                offset != 0,
                offset + messages.size != totalCount,
                totalCount,
                messages.size
            ),
            messages
        )
    }

    fun getTokens(bean: UserQLBean, @Min(1) count: Int, @GraphqlCursor after: String?): PushTokenQueryResult {
        val totalCount = pushTokenRepository.countByOwner(bean.uid)
        val offset =
            if (after.isNullOrBlank()) 0 else base64Decoder.decode(after).toString(Charsets.UTF_8).substring(6)
                .toInt() + 1
        val tokenList = pushTokenRepository.getPushTokenInfosByOwner(
            bean.uid,
            OffsetBasedPageRequest(offset, count)
        ).mapIndexed { index, item ->
            val indexInDatabase = index + offset
            val cursor = base64Encoder.encodeToString("cursor$indexInDatabase".toByteArray(Charsets.UTF_8))
            PushTokenItemWithCursor(item.id, item.pushToken, item.name, item.generatedAt, cursor)
        }
        return PushTokenQueryResult(
            QueryPageInfo(
                tokenList.firstOrNull()?.cursor,
                tokenList.firstOrNull()?.cursor,
                offset != 0,
                offset + tokenList.size != totalCount,
                totalCount,
                tokenList.size
            ),
            tokenList
        )
    }
}