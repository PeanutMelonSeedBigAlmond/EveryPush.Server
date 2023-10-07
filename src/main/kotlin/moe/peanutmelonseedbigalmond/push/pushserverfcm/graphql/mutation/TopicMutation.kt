package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicQLBean
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class TopicMutation : GraphQLMutationResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var topicRepository: TopicRepository

    fun createTopic(
        @Length(min = 5, max = 20) id: String,
        @NotBlank name: String,
        @NotBlank token: String
    ): TopicQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val pk = TopicInfo.TopicInfoPK()
        pk.topicId = id
        pk.owner = uid

        checkThrowGraphqlException(!topicRepository.existsByPk(pk)) { "Topic already exists" }

        val topic = TopicInfo()
        topic.name = name
        topic.pk = pk
        return topicRepository.save(topic).let {
            return@let TopicQLBean(it.pk.topicId, it.name)
        }
    }

    fun deleteTopic(@NotBlank id: String, @NotBlank token: String): List<TopicQLBean> {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        return topicRepository.deleteByPk(TopicInfo.TopicInfoPK(uid, id)).also {
            checkThrowGraphqlException(it.isNotEmpty()) { "Topic does not exists" }
        }.map {
            return@map TopicQLBean(it.pk.topicId, it.name)
        }
    }

    private inline fun checkThrowGraphqlException(value: Boolean, message: () -> String) {
        if (!value) {
            throw GraphqlException(message())
        }
    }
}