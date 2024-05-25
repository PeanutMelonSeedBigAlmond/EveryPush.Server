package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.MessageDetailResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.MessageExcerptInfoResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.PushMessageResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.MessageInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.service.PushMessageService
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.MarkdownUtil
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import org.hibernate.validator.constraints.Range
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/message")
@Validated
class MessageController {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var messageGroupRepository: MessageGroupRepository

    @Autowired
    private lateinit var keyRepository: KeyRepository

    @Autowired
    private lateinit var deviceInfoRepository: DeviceInfoRepository

    @Autowired
    private lateinit var pushMessageService: PushMessageService

    @Autowired
    private lateinit var userInfoRepository: UserInfoRepository

    /**
     * 列出指定分组的消息
     * @param messageGroupId String? null 未分类的消息
     * @param pageIndex Int
     * @param pageCount Int
     * @return List<MessageInfoResponse>
     */
    @PostMapping("list")
    fun listMessages(
        @RequestParam(required = false) messageGroupId: String?,
        @Min(0) pageIndex: Int,
        @Min(1) pageCount: Int,
    ): List<MessageExcerptInfoResponse> {
        val user = ThreadLocalUtil.getCurrentUser()

        val slice = if (messageGroupId != null) {
            if (!messageGroupRepository.existsByGroupIdAndUid(
                    messageGroupId,
                    user.uid
                )
            ) throw MessageGroupNotExistsException()
            messageRepository.findMessageInfosByUidAndMessageGroupId(
                user.uid, messageGroupId, PageRequest.of(
                    pageIndex, pageCount, Sort.by(Sort.Direction.DESC, "pushedAt")
                )
            )
        } else {
            messageRepository.findMessageInfosByMessageGroupIdIsNullAndUid(
                user.uid, PageRequest.of(
                    pageIndex, pageCount, Sort.by(Sort.Direction.DESC, "pushedAt")
                )
            )
        }
        return slice.content.map {
            MessageExcerptInfoResponse(
                it.id,
                it.uid,
                it.title,
                getMessageExcerpt(it.type, it.content),
                it.type,
                it.coverImgUrl,
                it.encrypted,
                it.messageGroupId,
                it.pushedAt
            )
        }
    }

    /**
     * 列出**所有**消息
     * @param pageIndex Int
     * @param pageCount Int
     * @param includeUngrouped Boolean
     * @return List<MessageInfoResponse>
     */
    @PostMapping("listAll")
    fun listAllMessages(
        @Min(0) pageIndex: Int,
        @Min(1) pageCount: Int,
        @RequestParam(defaultValue = "true") includeUngrouped: Boolean,
    ): List<MessageExcerptInfoResponse> {
        val user = ThreadLocalUtil.getCurrentUser()
        val slice = if (includeUngrouped) {
            messageRepository.findMessageInfosByUid(
                user.uid, PageRequest.of(
                    pageIndex, pageCount, Sort.by(Sort.Direction.DESC, "pushedAt")
                )
            )
        } else {
            messageRepository.findMessageInfosByUidAndMessageGroupIdIsNotNull(
                user.uid, PageRequest.of(
                    pageIndex, pageCount, Sort.by(Sort.Direction.DESC, "pushedAt")
                )
            )
        }
        return slice.content.map {
            MessageExcerptInfoResponse(
                it.id,
                it.uid,
                it.title,
                getMessageExcerpt(it.type, it.content),
                it.type,
                it.coverImgUrl,
                it.encrypted,
                it.messageGroupId,
                it.pushedAt
            )
        }
    }

    @PostMapping("remove")
    fun removeMessage(id: Long) {
        val user = ThreadLocalUtil.getCurrentUser()
        val message = messageRepository.findMessageInfoByIdAndUid(id, user.uid) ?: throw MessageNotExistsException()
        messageRepository.delete(message)

        logger.info("删除消息成功: user=${user.username}")
    }

    @RequestMapping(path = ["push"], method = [RequestMethod.GET, RequestMethod.POST])
    suspend fun pushMessage(
        @NotBlank key: String,
        @RequestParam(required = false) title: String?,
        @NotBlank content: String,
        @RequestParam(defaultValue = "Text") type: MessageType,
        @RequestParam(required = false) group: String?,
        @RequestParam(required = false) coverImgUrl: String?,
        @RequestParam(defaultValue = "5") @Range(min = 0, max = 10) priority: Int,
    ): PushMessageResponse {
        if (type == MessageType.Picture && !isValidUrl(content)) throw MalformedParametersException()

        // 图片类型，默认忽略封面
        var _coverImgUrl = coverImgUrl
        if (type == MessageType.Markdown) _coverImgUrl = null

        val uid = withContext(Dispatchers.IO) {
            keyRepository.findKeyInfoByKey(key)
        }?.uid ?: throw KeyNotExistsException()
        val user = withContext(Dispatchers.IO) {
            userInfoRepository.findById(uid).getOrNull() ?: throw UserNotExistsException()
        }

        val messageGroup = if (group != null) {
            withContext(Dispatchers.IO) {
                messageGroupRepository.findMessageGroupInfoByGroupIdAndUid(group, uid)
            } ?: throw MessageGroupNotExistsException()
        } else {
            null
        }

        val deviceTokens = withContext(Dispatchers.IO) {
            deviceInfoRepository.findDeviceInfosByUid(uid, null)
        }.content.map { it.deviceToken }

        var message = MessageInfo(
            title,
            content,
            type,
            priority,
            uid,
            _coverImgUrl,
            messageGroup?.groupId,
            false,
            System.currentTimeMillis()
        )
        message = withContext(Dispatchers.IO) {
            messageRepository.save(message)
        }

        val successCount =
            try {
                withContext(Dispatchers.IO) {
                    pushMessageService.pushMessage(
                        user.uid,
                        message.title,
                        message.content,
                        message.type,
                        message.priority,
                        message.coverImgUrl,
                        message.encrypted,
                        message.messageGroupId,
                        messageGroup?.name,
                        message.id,
                        deviceTokens
                    )
                }
            } catch (e: Exception) {
                throw MessageServiceException()
            }

        logger.info("推送消息成功: user=${user.username}, id=${message.id}")
        return PushMessageResponse(message.id, successCount)
    }

    @PostMapping("detail")
    fun messageDetail(id: Long): MessageDetailResponse {
        val user = ThreadLocalUtil.getCurrentUser()

        val message = messageRepository.findMessageInfoByIdAndUid(id, user.uid) ?: throw MessageNotExistsException()
        return MessageDetailResponse(
            message.id,
            message.title,
            message.content,
            message.coverImgUrl,
            message.type,
            message.pushedAt,
            message.uid,
            message.messageGroupId,
            message.encrypted
        )
    }

    fun getMessageExcerpt(messageType: MessageType, content: String): String {
        return when (messageType) {
            MessageType.Text -> content.take(40)
            MessageType.Picture -> content
            MessageType.Markdown -> MarkdownUtil.getMarkdownExcerpt(content, 40)
        }
    }

    private fun isValidUrl(str: String): Boolean {
        return Regex("^https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$").matches(str)
    }
}