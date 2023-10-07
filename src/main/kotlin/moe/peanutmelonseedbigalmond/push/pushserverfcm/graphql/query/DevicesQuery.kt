package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.DeviceRepository
import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenWrapper
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.DeviceQLBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Component
@Validated
class DevicesQuery : GraphQLQueryResolver {
    @Autowired
    private lateinit var loginTokenWrapper: LoginTokenWrapper

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    fun listDevices(@NotBlank token: String): List<DeviceQLBean> {
        val uid = loginTokenWrapper.getLoginTokenInfoByToken(token).belongsTo
        val deviceList = deviceRepository.getDeviceInfosByOwner(uid)
        return deviceList.map {
            return@map DeviceQLBean(it.id, it.deviceType, it.fcmToken, it.name)
        }
    }
}