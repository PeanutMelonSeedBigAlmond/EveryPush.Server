package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.MessageGroupDetailResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.MessageGroupResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.SyncMessageGroupResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.MessageGroupInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.MessageGroupRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.MessageRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.MessageGroupExistsException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.MessageGroupNotExistsException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@Validated
@RequestMapping("/api/messageGroup")
class MessageGroupController {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var messageGroupRepository: MessageGroupRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @PostMapping("create")
    fun createMessageGroup(@NotBlank name: String, @NotBlank id: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        if (messageGroupRepository.existsByGroupIdAndUid(id.trim(), user.uid)) throw MessageGroupExistsException()

        val messageGroup = MessageGroupInfo(name, id.trim(), user.uid, System.currentTimeMillis())
        messageGroupRepository.save(messageGroup)

        logger.info("创建消息组成功：user=${user.username}, groupName=${messageGroup.name}")
    }

    @PostMapping("rename")
    fun renameMessageGroup(@NotBlank id: String, @NotBlank name: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val messageGroup = messageGroupRepository.findMessageGroupInfoByGroupIdAndUid(id, user.uid)
            ?: throw MessageGroupNotExistsException()

        messageGroup.name = name
        messageGroupRepository.save(messageGroup)

        logger.info("重命名消息组成功：user=${user.username}, newGroupName=${messageGroup.name}")
    }

    @PostMapping("remove")
    fun removeMessageGroup(@NotBlank @NotNull id: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val messageGroup = messageGroupRepository.findMessageGroupInfoByGroupIdAndUid(id, user.uid)
            ?: throw MessageGroupNotExistsException()
        messageGroupRepository.delete(messageGroup)
        logger.info("删除消息组成功：user=${user.username}, groupName=${messageGroup.name}")
    }

    @PostMapping("list")
    fun listMessageGroup(
        @RequestParam(defaultValue = "0") pageIndex: Int,
        @RequestParam(defaultValue = "20") pageCount: Int
    ): List<MessageGroupResponse> {
        val user = ThreadLocalUtil.getCurrentUser()
        val slice = messageGroupRepository.findMessageGroupInfosByUid(user.uid, PageRequest.of(pageIndex, pageCount))
        val messageGroups = slice.content
        return messageGroups.map {
            MessageGroupResponse(it.id, it.uid, it.groupId, it.name, it.createdAt)
        }
    }

    @PostMapping("info")
    fun messageGroupInfo(
        @RequestParam(required = false, defaultValue = "null") id: String?
    ): MessageGroupDetailResponse {
        val user = ThreadLocalUtil.getCurrentUser()
        if (id != null) {
            val groupInfo = messageGroupRepository.findMessageGroupInfoByGroupIdAndUid(id, user.uid)
                ?: throw MessageGroupNotExistsException()
            val messageCount = messageRepository.countMessageInfosByMessageGroupIdAndUid(id, user.uid)
            return MessageGroupDetailResponse(groupInfo.groupId, groupInfo.id, groupInfo.name, messageCount)
        } else {
            val messageCount = messageRepository.countMessageInfosByMessageGroupIdIsNullAndUid(user.uid)
            return MessageGroupDetailResponse(null, null, null, messageCount)
        }
    }

    @PostMapping("sync")
    fun syncMessageGroup(
        @RequestParam clientMessageGroupsId: List<String>,
        @RequestParam clientMessageGroupsName: List<String>,
    ): SyncMessageGroupResponse {
        val user = ThreadLocalUtil.getCurrentUser()
        val clientMessageGroups = clientMessageGroupsId.zip(clientMessageGroupsName).toMap()
        val databaseMessageGroups = messageGroupRepository.findMessageGroupInfosByUid(user.uid, null)
            .associate {
                it.groupId to it.name
            }
        val messageGroupToBeDeleted = clientMessageGroups.filter {
            databaseMessageGroups[it.key] == null
        }
        val messageGroupToBeCreated = databaseMessageGroups.filter {
            clientMessageGroups[it.key] == null
        }
        val messageGroupToBeRenamed = databaseMessageGroups.filter {
            clientMessageGroups[it.key] != null && clientMessageGroups[it.key] != it.value
        }

        return SyncMessageGroupResponse(
            deleted = messageGroupToBeDeleted.toList().map { SyncMessageGroupResponse.Item(it.first, it.second) },
            renamed = messageGroupToBeRenamed.toList().map { SyncMessageGroupResponse.Item(it.first, it.second) },
            created = messageGroupToBeCreated.toList().map { SyncMessageGroupResponse.Item(it.first, it.second) }
        )
    }
}