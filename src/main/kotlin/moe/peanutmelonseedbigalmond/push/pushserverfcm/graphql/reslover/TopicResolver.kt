package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.OffsetBasedPageRequest
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.GraphqlCursor
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepositoryWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Min

@Validated
@Component
class TopicResolver : GraphQLResolver<TopicQLBean> {
    @Autowired
    protected lateinit var messageRepositoryWrapper: MessageRepositoryWrapper

    private val base64Encoder by lazy { Base64.getEncoder() }
    private val base64Decoder by lazy { Base64.getDecoder() }

    fun getLatestMessage(topic: TopicQLBean): LatestMessagesQLBean? {
        return messageRepositoryWrapper.queryLatestMessage(topic.owner, topic.id)?.let {
            return@let LatestMessagesQLBean(it.messageId, it.type, it.text, it.title, it.pushTime)
        }
    }

    fun getMessages(topic: TopicQLBean, @Min(1) count: Int, @GraphqlCursor after: String?): MessageQueryResult {
        val totalCount = messageRepositoryWrapper.countByTopicIdAndOwnerAndDeleted(topic.id, topic.owner, false)
        val offset =
            if (after.isNullOrBlank()) 0 else base64Decoder.decode(after).toString(Charsets.UTF_8).substring(6)
                .toInt() + 1
        val messages = messageRepositoryWrapper.queryMessageByOwnerAndTopicIdWithPaging(
            topic.owner,
            topic.id,
            OffsetBasedPageRequest(offset, count)
        ).mapIndexed { index, item ->
            val indexInDatabase = index + offset
            val cursor = base64Encoder.encodeToString("cursor$indexInDatabase".toByteArray(Charsets.UTF_8))
            MessageItemWithCursor(topic.owner, item.messageId, item.type, item.text, item.title, item.pushTime, cursor)
        }
        return MessageQueryResult(
            QueryPageInfo(
                messages.first().cursor,
                messages.last().cursor,
                offset != 0,
                offset + messages.size != totalCount,
                totalCount,
                messages.size
            ),
            messages
        )
    }
}
