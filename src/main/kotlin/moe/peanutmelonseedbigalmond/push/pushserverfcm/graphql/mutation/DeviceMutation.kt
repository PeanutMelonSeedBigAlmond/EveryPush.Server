package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.DeviceItem
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.DeviceRegisterParams
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.DeviceRenameParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Component
@Validated
class DeviceMutation : GraphQLMutationResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    fun registerDevice(@Valid params: DeviceRegisterParams): DeviceItem {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(params.token).belongsTo
        val device = deviceRepository.getDeviceInfoByFcmToken(params.deviceId)
        val savedDevice = if (device == null) {
            val deviceInfo = DeviceInfo()
            deviceInfo.deviceType = params.type!!
            deviceInfo.name = params.name
            deviceInfo.owner = uid
            deviceInfo.fcmToken = params.deviceId
            val saved = deviceRepository.save(deviceInfo)
            saved
        } else {
            device.name = params.name
            device.owner = uid
            device.deviceType = params.type!!
            device
        }
        return savedDevice.let {
            return@let DeviceItem(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }

    fun renameDevice(@Valid params: DeviceRenameParams): DeviceItem {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(params.token).belongsTo
        val deviceInfo =
            deviceRepository.findByIdAndOwner(params.id, uid) ?: throw GraphqlException("Device does not exists")
        deviceInfo.name = params.newName
        val saved = deviceRepository.save(deviceInfo)
        return saved.let {
            return@let DeviceItem(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }

    fun removeDevice(@NotBlank token: String, id: Long): DeviceItem {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val removed = deviceRepository.deleteByIdAndOwner(id, uid)
        return removed.firstOrNull()?.let {
            return@let DeviceItem(it.id, it.deviceType, it.fcmToken, it.name)
        } ?: throw GraphqlException("device does not exists")
    }
}