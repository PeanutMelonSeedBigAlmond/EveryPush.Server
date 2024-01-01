package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.OffsetBasedPageRequest
import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.GraphqlCursor
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Component
@Validated
class TopicQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var topicRepository: TopicRepository
    private val base64Decoder by lazy { Base64.getDecoder() }
    private val base64Encoder by lazy { Base64.getEncoder() }

    fun listTopic(@NotBlank token: String, @Min(1) count: Int, @GraphqlCursor after: String?): TopicQueryResult {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val totalCount = topicRepository.countByPk_Owner(uid)
        val offset =
            if (after.isNullOrBlank()) 0 else base64Decoder.decode(after).toString(Charsets.UTF_8).substring(6)
                .toInt() + 1
        val topics = topicRepository.findByPk_Owner(
            uid,
            OffsetBasedPageRequest(offset, count)
        ).mapIndexed { index, item ->
            val indexInDatabase = index + offset
            val cursor = base64Encoder.encodeToString("cursor$indexInDatabase".toByteArray(Charsets.UTF_8))
            TopicItemWithCursor(item.pk.topicId, item.name, item.pk.owner, cursor)
        }
        return TopicQueryResult(
            QueryPageInfo(
                topics.firstOrNull()?.cursor,
                topics.firstOrNull()?.cursor,
                offset != 0,
                offset + topics.size != totalCount,
                totalCount,
                topics.size
            ),
            topics
        )
    }

    fun topicDetail(@NotBlank token: String, topicId: String?): BaseTopicItem {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val topic = topicRepository.findByPk(TopicInfo.TopicInfoPK(uid, topicId))
            ?: throw GraphqlException("topic does not exists")

        return topic.let {
            return@let TopicItem(it.pk.topicId, it.name, uid)
        }
    }
}