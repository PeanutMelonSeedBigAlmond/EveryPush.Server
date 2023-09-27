package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 将参数中的token替换成对应用户的uid返回给controller
 * 并移除参数中的uid
 * @property loginTokenRepository LoginTokenRepository
 */
@Component
class ParamFilter : OncePerRequestFilter() {
    companion object {
        @JvmStatic
        private val excludePath = arrayOf("/ping", "/user/login", "/message/push")
    }

    @Autowired
    lateinit var loginTokenRepository: LoginTokenRepository

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath in excludePath) {
            filterChain.doFilter(request, response)
        } else {
            val token = request.getParameter("token")
            if (token.isNullOrBlank()) {
                filterChain.doFilter(request, response)
            } else {
                val uid =
                    loginTokenRepository.getLoginTokenInfoByToken(token)?.belongsTo ?: return filterChain.doFilter(
                        request,
                        response
                    )
                val wrapper = RequestParameterWrapper(request)
                wrapper.removeParameter("uid")
                wrapper.addParameters(mapOf("uid" to uid))
                filterChain.doFilter(wrapper, response)
            }
        }
    }
}