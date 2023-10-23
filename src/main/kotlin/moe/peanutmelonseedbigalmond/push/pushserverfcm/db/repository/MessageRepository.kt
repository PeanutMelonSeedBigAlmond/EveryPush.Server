package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface MessageRepository : CrudRepository<MessageBean, Long> {

    @Query("select m from MessageBean m where m.owner=?1 and m.deleted=false")
    fun findByOwnerAndNotDeleted(owner: Long): List<MessageBean>

    @Query("select m from MessageBean m where m.topicId=?1 and m.owner=?2 and m.deleted=false order by m.pushTime desc")
    fun queryMessagesByTopicIdAndOwnerAndPushTimeDesc(topicId: String, owner: Long): List<MessageBean>

    @Query("select m from MessageBean m where m.topicId is null and m.owner=?1 and m.deleted=false order by m.pushTime desc")
    fun queryMessagesByTopicIsNullIdAndOwnerAndPushTimeDesc(owner: Long): List<MessageBean>

    @Query("select m from MessageBean m where m.deleted=false and m.owner=?1 and m.messageId=?2")
    fun findByMessageIdAndOwnerAndNotDeleted(owner: Long, id: Long): MessageBean?

    @Transactional
    @Modifying
    @Query("update MessageBean m set m.topicId=null where m.topicId=?1 and m.owner=?2")
    fun moveTopicAllMessageToDefaultTopic(topicId: String, owner: Long): Int
}