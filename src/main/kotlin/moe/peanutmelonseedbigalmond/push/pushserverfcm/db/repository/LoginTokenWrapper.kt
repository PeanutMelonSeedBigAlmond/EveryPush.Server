package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.LoginTokenInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoginTokenWrapper(@Autowired private val loginTokenRepository: LoginTokenRepository) :
    LoginTokenRepository by loginTokenRepository {
    override fun getLoginTokenInfoByToken(token: String): LoginTokenInfo {
        val t = loginTokenRepository.getLoginTokenInfoByToken(token) ?: throw Exception("Token does not exists")
        if (t.expiredAt <= System.currentTimeMillis()) {
            throw Exception("Token expired")
        }

        t.expiredAt += LoginTokenInfo.validation
        return loginTokenRepository.save(t)
    }
}