package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class TopicQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var topicRepository: TopicRepository

    fun listTopic(@NotBlank token: String): List<TopicQLBean> {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val topicList = topicRepository.findByPk_Owner(uid)

        return topicList.map {
            return@map TopicQLBean(it.pk.topicId, it.name, uid)
        } + TopicQLBean(null, null, uid)
    }

    fun topicDetail(@NotBlank token: String, topicId: String?): TopicQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        if (topicId == null) {
            return TopicQLBean(null, null, uid)
        }
        val topic = topicRepository.findByPk(TopicInfo.TopicInfoPK(uid, topicId))
            ?: throw GraphqlException("topic does not exists")

        return topic.let {
            return@let TopicQLBean(it.pk.topicId, it.name, uid)
        }
    }
}