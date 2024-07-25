package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.DeviceInfoResponse
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.DeviceInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository.DeviceInfoRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.exception.DeviceNotExistsException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.ThreadLocalUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/api/device")
@Validated
class DeviceController {
    private val logger = Logger.getLogger(this::class.simpleName)

    @Autowired
    private lateinit var deviceInfoRepository: DeviceInfoRepository

    @PostMapping("register")
    fun registerDevice(
        @NotBlank deviceToken: String,
        @NotBlank name: String,
        @NotBlank @RequestParam(defaultValue = "Android") platform: String,
    ) {
        val user = ThreadLocalUtil.getCurrentUser()
        val userToken = ThreadLocalUtil.getCurrentUserToken()
        val deviceInfo = deviceInfoRepository.findDeviceInfoByDeviceToken(deviceToken)
        if (deviceInfo == null) {
            val info = DeviceInfo(
                user.uid,
                name,
                platform,
                deviceToken,
                userToken.id
            )
            deviceInfoRepository.save(info)
            logger.info("注册设备成功：username=${user.username}, deviceName=${info.name}, platform=${info.platform}")
        } else {
            if (deviceInfo.uid != user.uid) {
                deviceInfo.uid = user.uid
                deviceInfo.userTokenId = userToken.id
                deviceInfoRepository.save(deviceInfo)
                logger.warning("修改设备归属成功：username=${user.username}, deviceName=${deviceInfo.name}, platform=${deviceInfo.platform}")
            }
        }
    }

    @PostMapping("list")
    fun listDevices(
        @Min(0) pageIndex: Int,
        @Min(1) pageSize: Int,
    ): List<DeviceInfoResponse> {
        val user = ThreadLocalUtil.getCurrentUser()
        val deviceList = deviceInfoRepository.findDeviceInfosByUid(user.uid, PageRequest.of(pageIndex, pageSize))
        return deviceList.content.map {
            DeviceInfoResponse(it.id, it.uid, it.name, it.platform, it.deviceToken)
        }
    }

    @PostMapping("remove")
    fun removeDevice(@NotNull id: Long) {
        val user = ThreadLocalUtil.getCurrentUser()
        val device = deviceInfoRepository.findDeviceInfoByIdAndUid(id, user.uid) ?: throw DeviceNotExistsException()
        deviceInfoRepository.delete(device)

        logger.info("删除设备成功: username=${user.username}, deviceName=${device.name}")
    }

    @PostMapping("rename")
    fun renameDevice(@NotNull id: Long, @NotBlank name: String) {
        val user = ThreadLocalUtil.getCurrentUser()
        val device = deviceInfoRepository.findDeviceInfoByIdAndUid(id, user.uid) ?: throw DeviceNotExistsException()
        device.name = name
        deviceInfoRepository.save(device)

        logger.info("重命名设备成功: username=${user.username}, newDeviceName=${device.name}")
    }
}