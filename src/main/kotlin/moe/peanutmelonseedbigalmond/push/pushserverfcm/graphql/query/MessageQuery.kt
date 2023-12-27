package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.MessageItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class MessageQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenQuery: LoginTokenWrapper

    @Autowired
    private lateinit var messageRepository: MessageRepository

    fun queryMessage(@NotBlank token: String, id: Long): MessageItem {
        val uid = loginTokenQuery.getLoginTokenInfoByToken(token).belongsTo
        val message = messageRepository.findByMessageIdAndOwnerAndNotDeleted(uid, id)
            ?: throw GraphqlException("message does not exists")
        return message.let {
            return@let MessageItem(it.owner, it.messageId, it.type, it.text, it.title, it.pushTime)
        }
    }
}