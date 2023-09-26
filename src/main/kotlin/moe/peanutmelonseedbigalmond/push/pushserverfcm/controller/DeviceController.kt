package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueList
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.DeviceRegisterResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.FetchDeviceResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.logging.Logger
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/device")
@Validated
class DeviceController {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    /**
     * 列出指定用户的所有设备
     * @param uid Long
     * @return ResponseEntity<ResponseWrapper<List<FetchDeviceResponse>>>
     */
    @RequestMapping("/list")
    suspend fun fetchAllDevice(@NotNull uid: Long): ResponseEntity<ResponseWrapper<List<FetchDeviceResponse>>> {
        val device = deviceRepository.getDeviceInfosByOwner(uid).map {
            return@map FetchDeviceResponse(it.id, it.fcmToken, it.name)
        }
        return ResponseEntity(ResponseWrapper(data = device), HttpStatus.OK)
    }

    /**
     * 注册设备，不允许重复注册
     * @param uid Long
     * @param name String
     * @param fcmToken String
     * @param deviceType String
     * @return ResponseEntity<ResponseWrapper<DeviceRegisterResponse>>
     */
    @RequestMapping("/register")
    suspend fun registerDevice(
        @NotNull uid: Long,
        @NotEmpty name: String,
        @NotEmpty fcmToken: String,
        @ValueList(values = ["android"]) deviceType: String
    ): ResponseEntity<ResponseWrapper<DeviceRegisterResponse>> {
        val deviceRecord = deviceRepository.getDeviceInfoByFcmToken(fcmToken)
        if (deviceRecord == null) {
            // 设备未注册，注册设备
            val deviceInfo = DeviceInfo()
            deviceInfo.name = name
            deviceInfo.fcmToken = fcmToken
            deviceInfo.owner = uid
            deviceInfo.deviceType = deviceType
            val savedDeviceInfo = deviceRepository.save(deviceInfo)
            Logger.getLogger(this::class.java.name).info("用户$uid 的设备 $name 注册成功")
            return ResponseEntity(
                ResponseWrapper(
                    data = DeviceRegisterResponse(
                        savedDeviceInfo.id,
                        savedDeviceInfo.owner,
                        savedDeviceInfo.name,
                        savedDeviceInfo.fcmToken
                    )
                ), HttpStatus.OK
            )
        } else {
            return ResponseEntity(
                ResponseWrapper(
                    message = "设备已被注册"
                ), HttpStatus.BAD_REQUEST
            )
        }
    }

    /**
     * 重命名设备
     * @param uid Long
     * @param id Long
     * @param name String
     * @return ResponseEntity<ResponseWrapper<FetchDeviceResponse>>
     */
    @RequestMapping("/rename")
    suspend fun deviceRename(
        @NotNull uid: Long,
        @NotNull id: Long,
        @NotEmpty name: String,
    ): ResponseEntity<ResponseWrapper<FetchDeviceResponse>> {
        val deviceInfo = deviceRepository.getDeviceInfoById(id)
        return if (deviceInfo == null) {
            ResponseEntity(ResponseWrapper(message = "指定的设备不存在"), HttpStatus.BAD_REQUEST)
        } else if (deviceInfo.owner != uid) {
            ResponseEntity(ResponseWrapper(message = "非法操作"), HttpStatus.FORBIDDEN)
        } else {
            deviceInfo.name = name
            val savedDevice = deviceRepository.save(deviceInfo)

            return ResponseEntity(
                ResponseWrapper(
                    data = FetchDeviceResponse(
                        savedDevice.id,
                        savedDevice.fcmToken,
                        savedDevice.name
                    )
                ), HttpStatus.OK
            )
        }
    }

    /**
     * 删除指定的设备
     * @param uid Long
     * @param id Long
     * @return ResponseEntity<ResponseWrapper<Unit>>
     */
    @RequestMapping("/remove")
    suspend fun removeDevice(
        @NotNull uid: Long,
        @NotNull id: Long
    ): ResponseEntity<ResponseWrapper<Unit>> {
        val deviceInfo = deviceRepository.getDeviceInfoById(id)
        return if (deviceInfo == null) {
            ResponseEntity(ResponseWrapper(message = "指定的设备不存在"), HttpStatus.BAD_REQUEST)
        } else if (deviceInfo.owner != uid) {
            ResponseEntity(ResponseWrapper(message = "非法操作"), HttpStatus.FORBIDDEN)
        } else {
            deviceRepository.delete(deviceInfo)
            ResponseEntity(ResponseWrapper(data = Unit), HttpStatus.OK)
        }
    }
}