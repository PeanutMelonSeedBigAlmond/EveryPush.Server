package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.MessageBean
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<MessageBean, Long> {
    fun getMessageBeansByOwner(owner: Long): List<MessageBean>
    fun deleteMessageBeansByOwner(owner: Long):List<MessageBean>
    fun getMessageBeanByMessageId(messageId:Long):MessageBean?
}