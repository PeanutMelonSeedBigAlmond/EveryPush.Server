package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.LoginTokenInfo
import org.springframework.data.repository.CrudRepository

interface LoginTokenRepository:CrudRepository<LoginTokenInfo,Long> {
    fun getLoginTokenInfoByToken(token:String):LoginTokenInfo?
}