package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.UserLoginResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserToken
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.UserTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
@ConditionalOnProperty("debug")
class MockLoginController {
    @Autowired
    private lateinit var userTokenRepository: UserTokenRepository

    @GetMapping("mockLogin")
    fun mockLogin(): UserLoginResponse {
        val time = System.currentTimeMillis()
        val token = TokenUtils.randomToken()
        val bean = UserToken(
            token = token,
            uid = "",
            createdAt = time,
            updatedAt = time,
            deviceName = "mock",
            devicePlatform = "mock",
            isMockLogin = true
        )
        userTokenRepository.save(bean)
        return UserLoginResponse(token)
    }
}