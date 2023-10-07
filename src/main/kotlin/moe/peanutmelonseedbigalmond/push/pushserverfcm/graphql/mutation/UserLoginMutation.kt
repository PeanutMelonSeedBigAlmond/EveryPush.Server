package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import com.google.firebase.auth.FirebaseAuth
import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.LoginTokenInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.UserInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.UserRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TokenQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.logging.Logger
import javax.validation.constraints.NotBlank

@Component
@Validated
class UserLoginMutation : GraphQLMutationResolver {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper
    fun login(@NotBlank firebaseToken: String): TokenQLBean {
        // 验证token，获取用户uid
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken)
        val uid = decodedToken.uid
        // 通过 uid 获取用户信息
        val userRecord = FirebaseAuth.getInstance().getUser(uid)
        // 获取用户名，否则获取用户电子邮箱
        val username = userRecord.providerData.firstOrNull { it.displayName?.isNotBlank() ?: false }?.displayName
            ?: userRecord.email

        val recordUser = userRepository.getUserInfoByFirebaseUID(uid)
        val savedUserInfo: UserInfo?
        if (recordUser == null) {
            // 新建用户
            val userInfo = UserInfo()
            userInfo.firebaseUID = uid
            userInfo.username = username
            savedUserInfo = userRepository.save(userInfo)

            logger.info("用户注册成功: $username, uid=${savedUserInfo.uid}")
        } else {
            savedUserInfo = userRepository.getUserInfoByFirebaseUID(uid)
        }
        val token = generateLoginToken(savedUserInfo!!)
        logger.info("用户登录: $username")
        return TokenQLBean(token.belongsTo, token.token, token.expiredAt)
    }

    private fun generateLoginToken(user: UserInfo): LoginTokenInfo {
        val newToken = LoginTokenInfo()
        newToken.expiredAt = System.currentTimeMillis() + LoginTokenInfo.validation
        newToken.belongsTo = user.uid
        loginTokenWrapper.save(newToken)
        return newToken
    }
}