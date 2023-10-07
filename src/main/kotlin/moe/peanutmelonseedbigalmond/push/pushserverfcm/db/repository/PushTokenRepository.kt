package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.PushTokenInfo
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface PushTokenRepository : CrudRepository<PushTokenInfo, Long> {
    fun getPushTokenInfosByOwner(owner: Long): List<PushTokenInfo>
    fun getPushTokenInfoByPushToken(token: String): PushTokenInfo?
    fun getPushTokenInfoById(id: Long): PushTokenInfo?

    @Transactional
    @Modifying
    fun deleteByOwnerAndId(owner: Long, id: Long): List<PushTokenInfo>


    fun findByIdAndOwner(id: Long, owner: Long): PushTokenInfo?
}