package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.DeviceInfo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface DeviceInfoRepository : CrudRepository<DeviceInfo, Long> {
    fun findDeviceInfosByUid(uid: String, pageable: Pageable?): Slice<DeviceInfo>
    fun findDeviceInfoByIdAndUid(id: Long, uid: String): DeviceInfo?
    fun findDeviceInfoByDeviceToken(deviceToken: String): DeviceInfo?

    @Transactional
    @Modifying
    fun deleteByUserTokenId(userTokenId: Long): Int

    @Transactional
    @Modifying
    fun deleteByUserTokenIdNot(userTokenId: Long): Int
}