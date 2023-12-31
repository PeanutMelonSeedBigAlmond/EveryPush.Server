package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface DeviceRepository : CrudRepository<DeviceInfo, Long> {
    fun getDeviceInfosByOwner(owner: Long, pageable: Pageable): List<DeviceInfo>

    @Query(value = "select device.fcmToken from DeviceInfo device where device.owner=?1")
    fun getAllDevicesFcmTokenByOwner(uid: Long): List<String>
    fun getDeviceInfoByFcmToken(fcmToken: String): DeviceInfo?
    fun getDeviceInfoById(id: Long): DeviceInfo?
    fun findByIdAndOwner(id: Long, owner: Long): DeviceInfo?

    @Transactional
    @Modifying
    fun deleteByIdAndOwner(id: Long, owner: Long): List<DeviceInfo>
    fun countByOwner(owner: Long): Int
}