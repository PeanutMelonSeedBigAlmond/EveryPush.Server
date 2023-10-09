package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.LatestMessagesQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessagesQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TopicResolver : GraphQLResolver<TopicQLBean> {
    @Autowired
    protected lateinit var messageRepository: MessageRepository

    fun getLatestMessage(topic: TopicQLBean): LatestMessagesQLBean? {
        val latestMessage = if (topic.id == null) {
            messageRepository.queryMessagesByTopicIsNullIdAndOwnerAndPushTimeDesc(topic.owner)
        } else {
            messageRepository.queryMessagesByTopicIdAndOwnerAndPushTimeDesc(topic.id, topic.owner)
        }.firstOrNull()
        return latestMessage?.let {
            return@let LatestMessagesQLBean(it.messageId, it.type, it.text, it.title, it.pushTime)
        }
    }

    fun getMessages(topic: TopicQLBean): List<MessagesQLBean> {
        return if (topic.id == null) {
            messageRepository.queryMessagesByTopicIsNullIdAndOwnerAndPushTimeDesc(topic.owner)
        } else {
            messageRepository.queryMessagesByTopicIdAndOwnerAndPushTimeDesc(topic.id, topic.owner)
        }.map {
            return@map MessagesQLBean(topic.owner, it.messageId, it.type, it.text, it.title, it.pushTime, topic.id)
        }
    }
}
