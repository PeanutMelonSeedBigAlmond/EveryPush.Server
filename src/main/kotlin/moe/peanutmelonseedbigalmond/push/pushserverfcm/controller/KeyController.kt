package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.KeyResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.KeyInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.KeyRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.KeyNotExistsException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@Validated
@RestController
@RequestMapping("/api/key")
class KeyController {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var keyRepository: KeyRepository

    @PostMapping("generate")
    fun generateKey() {
        val user = ThreadLocalUtil.getCurrentUser()
        val name = TokenUtils.randomKeyName()
        val key = TokenUtils.randomToken()

        val keyInfo = KeyInfo(name, user.uid, key, System.currentTimeMillis())
        keyRepository.save(keyInfo)
        logger.info("创建 key 成功: username=${user.username}, name=${keyInfo.name}")
    }

    @PostMapping("rename")
    fun renameKey(@NotNull id: Long, @NotBlank name: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val keyInfo = keyRepository.findKeyInfoByIdAndUid(id, user.uid) ?: throw KeyNotExistsException()
        keyInfo.name = name
        keyRepository.save(keyInfo)

        logger.info("重命名 key 成功: username=${user.username}, newName=${keyInfo.name}")
    }

    @PostMapping("remove")
    fun removeKey(@NotNull id: Long) {
        val user = ThreadLocalUtil.getCurrentUser()
        val keyInfo = keyRepository.findKeyInfoByIdAndUid(id, user.uid) ?: throw KeyNotExistsException()
        keyRepository.delete(keyInfo)

        logger.info("删除 key 成功: username=${user.username}, name=${keyInfo.name}")
    }

    @PostMapping("reset")
    fun resetKey(@NotNull id: Long) {
        val user = ThreadLocalUtil.getCurrentUser()
        val keyInfo = keyRepository.findKeyInfoByIdAndUid(id, user.uid) ?: throw KeyNotExistsException()
        keyInfo.key = TokenUtils.randomToken()
        keyRepository.save(keyInfo)

        logger.info("重置 key 成功: username=${user.username}, name=${keyInfo.name}")
    }

    @PostMapping("list")
    fun listKey(
        @RequestParam(defaultValue = "0") pageIndex: Int,
        @RequestParam(defaultValue = "20") pageCount: Int
    ): List<KeyResponse> {
        val user = ThreadLocalUtil.getCurrentUser()
        val slice = keyRepository.findKeyInfosByUid(user.uid, PageRequest.of(pageIndex, pageCount))
        val keys = slice.content
        return keys.map {
            KeyResponse(it.id, it.name, it.key, it.createdAt, it.uid)
        }
    }
}