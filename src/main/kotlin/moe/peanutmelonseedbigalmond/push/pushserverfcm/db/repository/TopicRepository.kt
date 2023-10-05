package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import org.springframework.data.repository.CrudRepository

interface TopicRepository : CrudRepository<TopicInfo, TopicInfo.TopicInfoPK> {


    fun findByPk_Owner(owner: Long): List<TopicInfo>


    fun existsByPk(pk: TopicInfo.TopicInfoPK): Boolean


    fun findByPk(pk: TopicInfo.TopicInfoPK): TopicInfo?
}