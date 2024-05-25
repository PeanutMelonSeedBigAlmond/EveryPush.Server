package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.UserInfoRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.UserTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.InvalidUserLoginToken
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.UserNotExistsException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartRequest
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import kotlin.jvm.optionals.getOrNull

@Component
class UserInfoInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var userTokenRepository: UserTokenRepository

    @Autowired
    private lateinit var userInfoRepository: UserInfoRepository

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var token = request.getParameter("token")
        if (request is MultipartRequest) {
            token = token.removeSurrounding("\"")
        }
        if (token.isNullOrBlank()) {
            throw InvalidUserLoginToken()
        }
        val userToken = userTokenRepository.findUserTokenByToken(token) ?: throw InvalidUserLoginToken()
        val uid = userToken.uid
        val userInfo = userInfoRepository.findById(uid).getOrNull() ?: throw UserNotExistsException()
        ThreadLocalUtil.addCurrentUser(userInfo)

        userToken.updatedAt = System.currentTimeMillis()
        userInfoRepository.save(userInfo)

        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        ThreadLocalUtil.removeCurrentUser()
    }
}