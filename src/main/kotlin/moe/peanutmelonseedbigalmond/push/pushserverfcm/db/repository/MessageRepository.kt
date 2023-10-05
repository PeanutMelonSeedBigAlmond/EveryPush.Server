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

    fun findByOwnerAndDeleted(owner: Long, deleted: Boolean): List<MessageBean>

    @Transactional
    @Modifying
    @Query("update MessageBean m set m.deleted = ?1 where m.owner = ?2 and m.messageId = ?3")
    fun updateDeletedByOwnerAndMessageId(deleted: Boolean, owner: Long, messageId: Long): Int


    fun findByOwnerAndDeletedAndTopicId(owner: Long, deleted: Boolean, topicId: String): List<MessageBean>
}