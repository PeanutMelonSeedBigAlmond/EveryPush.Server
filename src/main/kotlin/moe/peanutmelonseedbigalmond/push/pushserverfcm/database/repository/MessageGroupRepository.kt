package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.MessageGroupInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.CrudRepository

interface MessageGroupRepository : CrudRepository<MessageGroupInfo, Long> {
    fun existsByGroupIdAndUid(groupId: String, uid: String): Boolean

    fun findMessageGroupInfoByGroupIdAndUid(groupId: String, uid: String): MessageGroupInfo?

    fun findMessageGroupInfosByUid(uid: String, pageable: Pageable?): Slice<MessageGroupInfo>
}