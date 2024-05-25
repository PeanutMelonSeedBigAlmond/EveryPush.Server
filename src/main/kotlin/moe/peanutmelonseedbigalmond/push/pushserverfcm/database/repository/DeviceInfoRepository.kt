package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.DeviceInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.CrudRepository

interface DeviceInfoRepository : CrudRepository<DeviceInfo, Long> {
    fun findDeviceInfosByUid(uid: String, pageable: Pageable?): Slice<DeviceInfo>
    fun findDeviceInfoByIdAndUid(id: Long, uid: String): DeviceInfo?
    fun findDeviceInfoByDeviceToken(deviceToken: String): DeviceInfo?
}