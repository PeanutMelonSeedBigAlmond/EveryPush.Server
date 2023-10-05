package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.CreateTopicResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ListTopicResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.TopicRepository
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/topic")
@Validated
class TopicController {
    @Autowired
    private lateinit var topicRepository: TopicRepository

    /**
     * 列出 topic
     * @param uid Long
     * @return ResponseEntity<ResponseWrapper<List<ListTopicResponse>>>
     */
    @RequestMapping("/list")
    suspend fun listTopics(
        @NotNull uid: Long
    ): ResponseEntity<ResponseWrapper<List<ListTopicResponse>>> {
        val result = topicRepository.findByPk_Owner(uid).map {
            return@map ListTopicResponse(
                id = it.pk.topicId,
                name = it.name,
            )
        }

        return ResponseEntity(ResponseWrapper(data = result), HttpStatus.OK)
    }

    @RequestMapping("/add")
    suspend fun createTopic(
        @NotNull uid: Long,
        @NotEmpty @Length(min = 5, max = 20) id: String,
        @NotEmpty name: String
    ): ResponseEntity<ResponseWrapper<CreateTopicResponse>> {
        val pk = TopicInfo.TopicInfoPK(owner = uid, topicId = id)
        val exists = topicRepository.existsByPk(pk)

        return if (exists) {
            ResponseEntity(ResponseWrapper(message = "已存在"), HttpStatus.BAD_REQUEST)
        } else {
            val topicData = TopicInfo()
            topicData.pk = pk
            topicData.name = name

            val saved = topicRepository.save(topicData)

            val result = CreateTopicResponse(
                id = saved.pk.topicId,
                name = saved.name,
            )
            ResponseEntity(ResponseWrapper(data = result), HttpStatus.OK)
        }
    }

    @RequestMapping("/remove")
    suspend fun removeTopic(
        @NotNull uid: Long,
        @NotNull id: String,
    ): ResponseEntity<ResponseWrapper<Unit>> {
        val entity = topicRepository.findByPk(TopicInfo.TopicInfoPK(owner = uid, topicId = id))
        return if (entity == null) {
            ResponseEntity(ResponseWrapper(message = "指定的 topic 不存在"), HttpStatus.BAD_REQUEST)
        } else {
            ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
        }
    }

    @RequestMapping("/edit")
    suspend fun editTopic(
        @NotNull uid: Long,
        @NotNull id: String,
        @NotNull name: String,
    ): ResponseEntity<ResponseWrapper<CreateTopicResponse>> {
        val data = topicRepository.findByPk(TopicInfo.TopicInfoPK(owner = uid, topicId = id))
        return if (data == null) {
            ResponseEntity(ResponseWrapper(message = " topic 不存在"), HttpStatus.BAD_REQUEST)
        } else {
            data.name = name
            val saved = topicRepository.save(data)

            val ret = CreateTopicResponse(id = data.pk.topicId, name = data.name)
            ResponseEntity(ResponseWrapper(data = ret), HttpStatus.OK)
        }
    }
}