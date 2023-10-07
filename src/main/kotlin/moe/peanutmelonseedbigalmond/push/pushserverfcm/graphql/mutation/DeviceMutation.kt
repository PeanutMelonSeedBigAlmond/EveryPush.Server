package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.DeviceInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.GraphqlException
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.DeviceQLBean
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

    fun registerDevice(@Valid params: DeviceRegisterParams): DeviceQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(params.token).belongsTo
        val deviceInfo = DeviceInfo()
        deviceInfo.deviceType = params.type!!
        deviceInfo.name = params.name
        deviceInfo.owner = uid
        deviceInfo.fcmToken = params.deviceId
        val saved = deviceRepository.save(deviceInfo)
        return saved.let {
            return@let DeviceQLBean(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }

    fun renameDevice(@Valid params: DeviceRenameParams): DeviceQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(params.token).belongsTo
        val deviceInfo =
            deviceRepository.findByIdAndOwner(params.id, uid) ?: throw GraphqlException("Device does not exists")
        deviceInfo.name = params.newName
        val saved = deviceRepository.save(deviceInfo)
        return saved.let {
            return@let DeviceQLBean(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }

    fun removeDevice(@NotBlank token: String, id: Long): DeviceQLBean {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val removed = deviceRepository.deleteByIdAndOwner(id, uid)
        return removed.firstOrNull()?.let {
            return@let DeviceQLBean(it.id, it.deviceType, it.fcmToken, it.name)
        } ?: throw GraphqlException("device does not exists")
    }
}