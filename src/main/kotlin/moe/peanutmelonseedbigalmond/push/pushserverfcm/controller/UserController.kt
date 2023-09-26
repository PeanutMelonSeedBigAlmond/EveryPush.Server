package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import com.google.firebase.auth.FirebaseAuth
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.LoginResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.UserInfoResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.LoginTokenInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.UserInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/user")
@Validated
class UserController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var loginTokenRepository: LoginTokenRepository

    @RequestMapping("/login")
    suspend fun loginOrRegister(
        @NotEmpty token: String
    ): ResponseEntity<ResponseWrapper<LoginResponse>> {
        // 验证token，获取用户uid
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
        val uid = decodedToken.uid
        // 通过 uid 获取用户信息
        val userRecord = FirebaseAuth.getInstance().getUser(uid)
        // 获取用户名，否则获取用户电子邮箱
        val username = userRecord.providerData.firstOrNull { it.displayName?.isNotBlank() ?: false }?.displayName
            ?: userRecord.email

        val recordUser = userRepository.getUserInfoByFirebaseUID(uid)
        var savedUserInfo:UserInfo?=null
        if (recordUser == null) {
            // 新建用户
            val userInfo = UserInfo()
            userInfo.firebaseUID = uid
            userInfo.username = username
            savedUserInfo=userRepository.save(userInfo)

            Logger.getLogger(this::class.java.name).info("用户注册成功: $username, uid=${savedUserInfo.uid}")
        }else{
            savedUserInfo=userRepository.getUserInfoByFirebaseUID(uid)
        }
        val newToken = generateLoginToken(savedUserInfo!!)
        Logger.getLogger(this::class.java.name).info("用户登录: $username")
        return ResponseEntity(ResponseWrapper(data = LoginResponse(newToken.token, newToken.expiredAt)), HttpStatus.OK)
    }

    private fun generateLoginToken(user:UserInfo): LoginTokenInfo {
        val newToken = LoginTokenInfo()
        newToken.expiredAt = System.currentTimeMillis() + LoginTokenInfo.validation
        newToken.belongsTo = user.uid
        loginTokenRepository.save(newToken)
        return newToken
    }

    /**
     * 获取用户信息
     * @param uid String
     * @return ResponseEntity<ResponseWrapper<FetchUserResponse>>
     */
    @RequestMapping("/info")
    suspend fun userInfo(@NotNull uid: Long): ResponseEntity<ResponseWrapper<UserInfoResponse>> {
        val userInfo = userRepository.getUserInfoByUid(uid)
            ?: return ResponseEntity(ResponseWrapper(message = "用户不存在"), HttpStatus.NOT_FOUND)
        return ResponseEntity(ResponseWrapper(data = UserInfoResponse(userInfo.uid,userInfo.username)), HttpStatus.OK)
    }
}