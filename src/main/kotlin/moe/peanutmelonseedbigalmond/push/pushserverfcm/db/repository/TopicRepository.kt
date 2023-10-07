package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.TopicInfo
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface TopicRepository : CrudRepository<TopicInfo, TopicInfo.TopicInfoPK> {


    fun findByPk_Owner(owner: Long): List<TopicInfo>


    fun existsByPk(pk: TopicInfo.TopicInfoPK): Boolean


    fun findByPk(pk: TopicInfo.TopicInfoPK): TopicInfo?

    @Transactional
    @Modifying
    fun deleteByPk(pk: TopicInfo.TopicInfoPK): List<TopicInfo>
}