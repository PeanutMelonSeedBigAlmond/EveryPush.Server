package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessagesQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class MessageMutation : GraphQLMutationResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var messageRepository: MessageRepository

    fun deleteMessage(
        @NotBlank token: String,
        id: Long
    ): MessagesQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val message = messageRepository.findByMessageIdAndOwnerAndNotDeleted(uid, id)
            ?: throw GraphqlException("message does not exists")
        messageRepository.delete(message)

        return message.let {
            return@let MessagesQLBean(it.owner, it.messageId, it.type, it.text, it.title, it.pushTime, it.topicId)
        }
    }
}