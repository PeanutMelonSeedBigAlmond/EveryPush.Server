package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class MessageRepositoryWrapper(@Autowired private val messageRepository: MessageRepository) :
    MessageRepository by messageRepository {
    fun queryMessageByOwnerAndTopicIdWithPaging(owner: Long, topicId: String?, pageable: Pageable): List<MessageBean> {
        return if (topicId == null) {
            messageRepository.queryMessagesByTopicIsNullIdAndOwnerAndPushTimeDesc(owner, pageable)
        } else {
            messageRepository.queryMessagesByTopicIdAndOwnerAndPushTimeDesc(topicId, owner, pageable)
        }
    }

    fun queryLatestMessage(owner: Long, topicId: String?): MessageBean? {
        return queryMessageByOwnerAndTopicIdWithPaging(owner, topicId, Pageable.ofSize(1)).firstOrNull()
    }
}