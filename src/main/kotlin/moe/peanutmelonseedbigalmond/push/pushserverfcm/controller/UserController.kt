package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.validation.constraints.NotBlank
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.UserInfoResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.UserLoginDeviceResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.UserLoginResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserToken
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.DeviceInfoRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.UserInfoRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.UserTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.InvalidCredentialException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/user")
@Validated
class UserController {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var userInfoRepository: UserInfoRepository

    @Autowired
    private lateinit var loginTokenRepository: UserTokenRepository

    @Autowired
    private lateinit var deviceInfoRepository: DeviceInfoRepository

    @PostMapping("login")
    fun login(
        @NotBlank idToken: String,
        @NotBlank @RequestParam(defaultValue = "Android") platform: String,
        @NotBlank @RequestParam(defaultValue = "Unknown") deviceName: String
    ): UserLoginResponse {
        val decodedToken = try {
            FirebaseAuth.getInstance()
                .verifyIdToken(idToken)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            logger.throwing(this::class.simpleName, "login", e)
            throw InvalidCredentialException()
        }
        val uid = decodedToken.uid
        val username = decodedToken.name
        val email = decodedToken.email
        val avatarUrl = decodedToken.picture
        registerUserOrUpdateInfo(uid, username, email, avatarUrl)

        val userLoginToken = TokenUtils.randomToken()
        val time = System.currentTimeMillis()
        val loginToken = UserToken(
            token = userLoginToken,
            uid = uid,
            createdAt = time,
            updatedAt = time,
            deviceName = deviceName,
            devicePlatform = platform,
            isMockLogin = false,
        )
        loginTokenRepository.save(loginToken)

        logger.info("用户登录：$username")
        return UserLoginResponse(userLoginToken)
    }

    @PostMapping("info")
    fun getUserInfo(): UserInfoResponse {
        val user = ThreadLocalUtil.getCurrentUser()
        val response = UserInfoResponse(user.uid, user.username, user.email, user.avatarUrl)
        return response
    }

    @PostMapping("logout")
    fun userLogout(@NotBlank token: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val userToken = ThreadLocalUtil.getCurrentUserToken()
        val count = loginTokenRepository.deleteUserTokenByUidAndToken(user.uid, token)
        deviceInfoRepository.deleteByUserTokenId(userToken.id)
        if (count != 0L) {
            logger.info("用户注销: ${user.username}")
        }
    }

    @PostMapping("logoutOthers")
    fun logoutOthers(@NotBlank token: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val userToken = ThreadLocalUtil.getCurrentUserToken()
        val count = loginTokenRepository.deleteUserTokenByUidAndTokenIsNot(user.uid, token)
        deviceInfoRepository.deleteByUserTokenIdNot(userToken.id)
        if (count != 0L) {
            logger.info("用户注销: ${user.username}")
        }
    }

    @PostMapping("clients")
    fun userLoginDevices(token: String): List<UserLoginDeviceResponse> {
        val user = ThreadLocalUtil.getCurrentUser()
        val tokenList = loginTokenRepository.findUserTokensByUid(user.uid)
        return tokenList.content.map {
            UserLoginDeviceResponse(it.token, it.deviceName, it.devicePlatform, it.updatedAt, it.token == token)
        }
    }

    private fun registerUserOrUpdateInfo(uid: String, username: String?, email: String?, avatarUrl: String?) {
        val userInfo = userInfoRepository.findById(uid).getOrNull()
        if (userInfo != null) {
            userInfo.username = username
            userInfo.email = email
            userInfo.avatarUrl = avatarUrl
            userInfoRepository.save(userInfo)
        } else {
            val info = UserInfo()
            info.uid = uid
            info.email = email
            info.username = username
            userInfoRepository.save(info)
        }
    }
}