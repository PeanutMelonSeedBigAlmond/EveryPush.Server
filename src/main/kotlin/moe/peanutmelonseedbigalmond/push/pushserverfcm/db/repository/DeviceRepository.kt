package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import org.springframework.data.repository.CrudRepository

interface DeviceRepository : CrudRepository<DeviceInfo, Long> {
    fun getDeviceInfosByOwner(owner: Long): List<DeviceInfo>
    fun getDeviceInfoByFcmToken(fcmToken: String): DeviceInfo?
    fun getDeviceInfoById(id: Long): DeviceInfo?


    fun findByIdAndOwner(id: Long, owner: Long): DeviceInfo?


    fun deleteByIdAndOwner(id: Long, owner: Long): List<DeviceInfo>
}