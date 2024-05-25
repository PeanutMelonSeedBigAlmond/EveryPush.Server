package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.KeyInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.CrudRepository

interface KeyRepository : CrudRepository<KeyInfo, Long> {
    fun findKeyInfoByIdAndUid(id: Long, uid: String): KeyInfo?

    fun findKeyInfosByUid(uid: String, pageable: Pageable): Slice<KeyInfo>

    fun findKeyInfoByKey(key: String): KeyInfo?
}