package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.PushTokenQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class PushTokenQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    fun listToken(@NotBlank token: String): List<PushTokenQLBean> {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        return pushTokenRepository.getPushTokenInfosByOwner(uid).map {
            return@map PushTokenQLBean(it.id, it.pushToken, it.name, it.generatedAt)
        }
    }
}