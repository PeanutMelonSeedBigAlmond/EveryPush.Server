package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.UserRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.UserQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class GenericUserQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenRepository: LoginTokenWrapper

    @Autowired
    private lateinit var userRepository: UserRepository

    fun getUser(@NotBlank token: String): UserQLBean {
        val tokenInfo = loginTokenRepository.getLoginTokenInfoByToken(token)
        val userInfo = userRepository.getUserInfoByUid(tokenInfo.id)!!

        return UserQLBean(userInfo.username, userInfo.uid)
    }
}