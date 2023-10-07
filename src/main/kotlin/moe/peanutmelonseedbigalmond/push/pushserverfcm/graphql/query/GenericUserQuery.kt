package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TokenQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class GenericUserQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenRepository: LoginTokenWrapper

    fun token(@NotBlank loginToken: String): TokenQLBean? {
        val token = loginTokenRepository.getLoginTokenInfoByToken(loginToken)

        return TokenQLBean(token.belongsTo, token.token, token.expiredAt)
    }
}