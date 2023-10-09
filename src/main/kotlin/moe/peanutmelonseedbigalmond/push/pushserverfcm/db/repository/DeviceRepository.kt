package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface DeviceRepository : CrudRepository<DeviceInfo, Long> {
    fun getDeviceInfosByOwner(owner: Long): List<DeviceInfo>
    fun getDeviceInfoByFcmToken(fcmToken: String): DeviceInfo?
    fun getDeviceInfoById(id: Long): DeviceInfo?


    fun findByIdAndOwner(id: Long, owner: Long): DeviceInfo?

    @Transactional
    @Modifying
    fun deleteByIdAndOwner(id: Long, owner: Long): List<DeviceInfo>
}