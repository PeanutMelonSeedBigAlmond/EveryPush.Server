package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.FetchPushTokenResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.PushTokenInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.PushTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import retrofit2.Response
import java.util.*
import java.util.logging.Logger
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@RestController
@RequestMapping("/token")
@Validated
class PushTokenController {
    @Autowired
    private lateinit var pushTokenRepository: PushTokenRepository

    /**
     * 新建 token
     * @param uid Long
     * @return ResponseEntity<ResponseWrapper<FetchPushTokenResponse>>
     */
    @RequestMapping("/generate")
    suspend fun generatePushToken(@NotNull uid: Long): ResponseEntity<ResponseWrapper<FetchPushTokenResponse>> {
        val token = PushTokenInfo()
        token.generatedAt = System.currentTimeMillis()
        token.owner = uid
        token.pushToken = randomPushToken()
        token.name = randomTokenName()

        val savedPushToken = pushTokenRepository.save(token)

        Logger.getLogger(this::class.java.name).info("用户 $uid 生成新 token: ${savedPushToken.name} 成功")

        return ResponseEntity(
            ResponseWrapper(
                data = FetchPushTokenResponse(
                    savedPushToken.id,
                    savedPushToken.pushToken,
                    savedPushToken.name,
                    savedPushToken.generatedAt
                )
            ), HttpStatus.CREATED
        )
    }

    /**
     * token 重命名
     * @param uid Long
     * @param id Long
     * @param name String
     * @return ResponseEntity<ResponseWrapper<FetchPushTokenResponse>>
     */
    @RequestMapping("/rename")
    suspend fun renameToken(
        @NotNull uid:Long,
        @NotNull id:Long,
        @NotEmpty name: String
    ): ResponseEntity<ResponseWrapper<FetchPushTokenResponse>> {
        val tokenInfo=pushTokenRepository.getPushTokenInfoById(id)
        return if (tokenInfo==null){
            ResponseEntity(ResponseWrapper(message = "token 不存在"),HttpStatus.BAD_REQUEST)
        }else if (tokenInfo.owner!=uid){
            ResponseEntity(ResponseWrapper(message = "非法操作"),HttpStatus.FORBIDDEN)
        }else{
            tokenInfo.name=name
            val savedToken=pushTokenRepository.save(tokenInfo)

            ResponseEntity(ResponseWrapper(data = FetchPushTokenResponse(savedToken.id,savedToken.pushToken,savedToken.name,savedToken.generatedAt)),HttpStatus.OK)
        }
    }

    /**
     * token 重置/重新生成
     * @param uid Long
     * @param id Long
     * @return ResponseEntity<ResponseWrapper<FetchPushTokenResponse>>
     */
    @RequestMapping("/reGenerate")
    suspend fun reGenerateToken(
        @NotNull uid: Long,
        @NotNull id: Long
    ):ResponseEntity<ResponseWrapper<FetchPushTokenResponse>>{
        val tokenInfo=pushTokenRepository.getPushTokenInfoById(id)
        return if (tokenInfo==null){
            ResponseEntity(ResponseWrapper(message = "token 不存在"),HttpStatus.BAD_REQUEST)
        }else if (tokenInfo.owner!=uid){
            ResponseEntity(ResponseWrapper(message = "非法操作"),HttpStatus.FORBIDDEN)
        }else{
            tokenInfo.pushToken=randomPushToken()
            val savedToken=pushTokenRepository.save(tokenInfo)
            ResponseEntity(ResponseWrapper(data = FetchPushTokenResponse(savedToken.id,savedToken.pushToken,savedToken.name,savedToken.generatedAt)),HttpStatus.OK)
        }
    }

    /**
     * token 撤销
     * @param firebaseUID String
     * @param id Int
     * @return ResponseEntity<ResponseWrapper<Unit>>
     */
    @RequestMapping("/revoke")
    suspend fun revokeToken(
        @NotNull uid:Long,
        @NotNull id: Long
    ): ResponseEntity<ResponseWrapper<Unit>> {
        val tokenInfo=pushTokenRepository.getPushTokenInfoById(id)
        return if (tokenInfo==null){
            ResponseEntity(ResponseWrapper(message = "token 不存在"),HttpStatus.BAD_REQUEST)
        }else if (tokenInfo.owner!=uid){
            ResponseEntity(ResponseWrapper(message = "非法操作"),HttpStatus.FORBIDDEN)
        }else{
            pushTokenRepository.delete(tokenInfo)
            ResponseEntity(ResponseWrapper(data = Unit),HttpStatus.OK)
        }
    }

    /**
     * 列出所有token
     * @param firebaseUID String
     * @return ResponseEntity<ResponseWrapper<Set<FetchPushTokenResponse>>>
     */
    @RequestMapping("/all")
    suspend fun fetchAllToken(@NotNull uid: Long): ResponseEntity<ResponseWrapper<List<FetchPushTokenResponse>>> {
        val tokenList=pushTokenRepository.getPushTokenInfosByOwner(uid).map {
            return@map FetchPushTokenResponse(it.id,it.pushToken,it.name,it.generatedAt)
        }
        return ResponseEntity(ResponseWrapper(data = tokenList),HttpStatus.OK)
    }

    private fun randomPushToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun randomTokenName(): String {
        val sb = StringBuffer("Key ")
        for (i in 1..5) {
            sb.append(characterList.random())
        }
        return sb.toString()
    }

    companion object {
        private val characterList = listOf('a'..'z', 'A'..'Z', '0'..'9').flatten()
    }
}