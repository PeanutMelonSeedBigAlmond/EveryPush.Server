package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.PushTokenInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushTokenQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushTokenRenameParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Component
@Validated
class PushTokenMutation : GraphQLMutationResolver {
    @Autowired
    private lateinit var loginTokenRepository: LoginTokenWrapper

    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository
    fun deleteToken(id: Long, @NotBlank token: String): PushTokenQLBean {
        val uid = loginTokenRepository.getLoginTokenInfoByToken(token).belongsTo
        return pushTokenRepository.deleteByOwnerAndId(uid, id).firstOrNull()?.let {
            return@let PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        } ?: throw GraphqlException("token does not exists")
    }

    fun reGenerateToken(id: Long, @NotBlank token: String): PushTokenQLBean {
        val uid = loginTokenRepository.getLoginTokenInfoByToken(token).belongsTo
        val pushToken = pushTokenRepository.findByIdAndOwner(id, uid) ?: throw GraphqlException("token does not exists")
        pushToken.pushToken = randomPushToken()
        return pushTokenRepository.save(pushToken).let {
            return@let PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        }
    }

    fun createToken(@NotBlank token: String): PushTokenQLBean {
        val uid = loginTokenRepository.getLoginTokenInfoByToken(token).belongsTo
        val pushToken = randomPushToken()
        val name = randomTokenName()
        val tokenInfo = PushTokenInfo()
        tokenInfo.owner = uid
        tokenInfo.name = name
        tokenInfo.pushToken = pushToken
        tokenInfo.generatedAt = System.currentTimeMillis()
        return pushTokenRepository.save(tokenInfo).let {
            return@let PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        }
    }

    fun renameToken(@Valid params: PushTokenRenameParams): PushTokenQLBean {
        val uid = loginTokenRepository.getLoginTokenInfoByToken(params.token).belongsTo
        val pushToken =
            pushTokenRepository.findByIdAndOwner(params.id, uid) ?: throw GraphqlException("push token does not exists")
        pushToken.name = params.newName
        val saved = pushTokenRepository.save(pushToken)
        return saved.let {
            return@let PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        }
    }

    private fun randomPushToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun randomTokenName(): String {
        val sb = StringBuffer("Key ")
        for (i in 1..5) {
            sb.append(characterList.random())
        }
        return sb.toString()
    }

    companion object {
        private val characterList = listOf('a'..'z', 'A'..'Z', '0'..'9').flatten()
    }
}