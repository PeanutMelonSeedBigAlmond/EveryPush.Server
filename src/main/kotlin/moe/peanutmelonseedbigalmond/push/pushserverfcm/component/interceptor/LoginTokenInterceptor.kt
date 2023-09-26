package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.interceptor

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.LoginTokenInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 验证参数中的token
 * @property loginTokenRepository LoginTokenRepository
 */
@Component
class LoginTokenInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var loginTokenRepository: LoginTokenRepository

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tokenStr = request.getParameter("token")
        if (tokenStr == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.setHeader("Content-Type", "application/json")
            response.outputStream.write("""{"message":"未登录"}""".toByteArray())
            response.outputStream.close()
            return false
        }

        val tokenInfo = loginTokenRepository.getLoginTokenInfoByToken(tokenStr)
        return if (tokenInfo == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.setHeader("Content-Type", "application/json")
            response.outputStream.write("""{"message":"token无效"}""".toByteArray())
            response.outputStream.close()
            false
        } else if (tokenInfo.expiredAt < System.currentTimeMillis()) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.setHeader("Content-Type", "application/json")
            response.outputStream.write("""{"message":"token已经过期"}""".toByteArray())
            response.outputStream.close()
            false
        } else {
            tokenInfo.expiredAt = System.currentTimeMillis() + LoginTokenInfo.validation
            loginTokenRepository.save(tokenInfo)
            true
        }
    }
}