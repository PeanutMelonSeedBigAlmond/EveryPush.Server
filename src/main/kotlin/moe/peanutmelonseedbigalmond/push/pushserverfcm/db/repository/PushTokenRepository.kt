package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.PushTokenInfo
import org.springframework.data.repository.CrudRepository

interface PushTokenRepository : CrudRepository<PushTokenInfo, Long> {
    fun getPushTokenInfosByOwner(owner: Long): List<PushTokenInfo>
    fun getPushTokenInfoByPushToken(token: String):PushTokenInfo?
    fun getPushTokenInfoById(id:Long):PushTokenInfo?
}