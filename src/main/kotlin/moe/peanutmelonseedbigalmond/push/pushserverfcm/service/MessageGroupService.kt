package moe.peanutmelonseedbigalmond.push.pushserverfcm.service

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.MessageGroupInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.MessageGroupRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.MessageGroupIdInvalidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MessageGroupService {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var messageGroupRepository: MessageGroupRepository

    fun createMessageGroupOrGet(user: UserInfo, id: String): MessageGroupInfo {
        if (!checkMessageGroupIsValid(id)) throw MessageGroupIdInvalidException()

        val exists = messageGroupRepository.findMessageGroupInfoByGroupIdAndUid(id, user.uid)
        if (exists == null) {
            // 默认 分组名 = 分组 id
            var messageGroup = MessageGroupInfo(id, id, user.uid, System.currentTimeMillis())
            messageGroup = messageGroupRepository.save(messageGroup)
            logger.info("自动创建消息组成功：user=${user.username}, groupId=${messageGroup.groupId}")
            return messageGroup
        } else {
            return exists
        }
    }

    private fun checkMessageGroupIsValid(id: String) = id.matches(Regex("[0-9a-zA-Z]+"))
}