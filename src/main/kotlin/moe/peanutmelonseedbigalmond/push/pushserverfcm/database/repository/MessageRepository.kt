package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.MessageInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<MessageInfo, Long> {
    fun findMessageInfosByUidAndMessageGroupId(
        uid: String,
        messageGroupId: String,
        pageable: Pageable
    ): Slice<MessageInfo>

    fun findMessageInfosByMessageGroupIdIsNullAndUid(uid: String, pageable: Pageable): Slice<MessageInfo>

    fun findMessageInfosByUidAndMessageGroupIdIsNotNull(uid: String, pageable: Pageable): Slice<MessageInfo>

    fun findMessageInfosByUid(uid: String, pageable: Pageable): Slice<MessageInfo>

    fun findMessageInfoByIdAndUid(id: Long, uid: String): MessageInfo?

    fun countMessageInfosByMessageGroupIdAndUid(messageGroupId: String, uid: String): Int

    fun countMessageInfosByMessageGroupIdIsNullAndUid(uid: String): Int
}