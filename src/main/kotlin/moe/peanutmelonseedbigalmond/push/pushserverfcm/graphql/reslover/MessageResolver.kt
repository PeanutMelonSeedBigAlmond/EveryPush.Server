package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessagesQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MessageResolver : GraphQLResolver<MessagesQLBean> {
    @Autowired
    private lateinit var topicRepository: TopicRepository

    fun getTopic(message: MessagesQLBean): TopicQLBean? {
        if (message.topicId == null) return null
        return topicRepository.findByPk(TopicInfo.TopicInfoPK(message.owner, message.topicId))?.let {
            return@let TopicQLBean(it.pk.topicId, it.name, message.owner)
        }
    }
}