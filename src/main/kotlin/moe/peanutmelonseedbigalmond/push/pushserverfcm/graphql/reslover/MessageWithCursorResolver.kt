package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepositoryWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessageItemWithCursor
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MessageWithCursorResolver : GraphQLResolver<MessageItemWithCursor> {
    @Autowired
    private lateinit var messageRepositoryWrapper: MessageRepositoryWrapper

    @Autowired
    private lateinit var topicRepository: TopicRepository

    fun getTopic(message: MessageItemWithCursor): TopicQLBean? {
        val messageInDatabase =
            messageRepositoryWrapper.findByMessageIdAndOwnerAndNotDeleted(message.owner, message.id)
                ?: throw GraphqlException("message does not exists")

        if (messageInDatabase.topicId == null) /*return TopicQLBean(null, null, messageInDatabase.owner)*/ return null
        val topic =
            topicRepository.findByPk(TopicInfo.TopicInfoPK(messageInDatabase.owner, messageInDatabase.topicId!!))
                ?: throw GraphqlException("topic does not exists")
        return TopicQLBean(topic.pk.topicId, topic.name, topic.pk.owner)
    }
}