package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface MessageRepository : CrudRepository<MessageBean, Long> {
    @Transactional
    @Modifying
    @Query("update MessageBean m set m.deleted = ?1 where m.owner = ?2")
    fun updateDeletedByOwner(deleted: Boolean, owner: Long): Int

    @Query("select m from MessageBean m where m.owner=?1 and m.deleted=false")
    fun findByOwnerAndNotDeleted(owner: Long): List<MessageBean>

    @Transactional
    @Modifying
    @Query("update MessageBean m set m.deleted = ?1 where m.owner = ?2 and m.messageId = ?3")
    fun updateDeletedByOwnerAndMessageId(deleted: Boolean, owner: Long, messageId: Long): Int


    fun findByOwnerAndDeletedAndTopicId(owner: Long, deleted: Boolean, topicId: String): List<MessageBean>

    @Query("select m from MessageBean m where m.topicId=?1 and m.owner=?2 and m.deleted=false order by m.pushTime desc")
    fun queryMessagesByTopicIdAndOwnerAndPushTimeDesc(topicId: String, owner: Long): List<MessageBean>

    @Query("select m from MessageBean m where m.topicId is null and m.owner=?1 and m.deleted=false order by m.pushTime desc")
    fun queryMessagesByTopicIsNullIdAndOwnerAndPushTimeDesc(owner: Long): List<MessageBean>
}