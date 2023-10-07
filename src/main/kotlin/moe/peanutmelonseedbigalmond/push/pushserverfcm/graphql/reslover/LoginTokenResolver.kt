package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.reslover

import graphql.kickstart.tools.GraphQLResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.UserRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TokenQLBean
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.UserQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoginTokenResolver : GraphQLResolver<TokenQLBean> {
    @Autowired
    private lateinit var userRepository: UserRepository
    fun getUser(tokenQLBean: TokenQLBean): UserQLBean? {
        return userRepository.getUserInfoByUid(tokenQLBean.uid)?.let {
            return@let UserQLBean(it.username, it.uid)
        }
    }
}