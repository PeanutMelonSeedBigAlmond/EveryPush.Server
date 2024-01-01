package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepositoryWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class BaseMessageResolver<T : BaseMessageItem> : GraphQLResolver<T> {
    @Autowired
    protected lateinit var topicRepository: TopicRepository

    @Autowired
    protected lateinit var messageRepositoryWrapper: MessageRepositoryWrapper

    fun getTopic(message: T): BaseTopicItem? {
        val messageInDatabase = messageRepositoryWrapper.findByMessageIdAndOwnerAndNotDeleted(message.owner, message.id)
            ?: throw GraphqlException("message does not exists")
        if (messageInDatabase.topicId == null) return null
        return topicRepository.findByPk(TopicInfo.TopicInfoPK(messageInDatabase.owner, messageInDatabase.topicId!!))
            ?.let {
                return@let TopicItem(it.pk.topicId, it.name, message.owner)
            }
    }
}

@Component
class MessageItemResolver : BaseMessageResolver<MessageItem>()

@Component
class MessageItemWithCursorResolver : BaseMessageResolver<MessageItemWithCursor>()