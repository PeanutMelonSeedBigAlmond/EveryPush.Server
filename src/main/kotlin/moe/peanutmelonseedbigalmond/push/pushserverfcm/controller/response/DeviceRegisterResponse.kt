package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class DeviceRegisterResponse(
    val id: Long,
    val uid: Long,
    val name: String,
    val deviceId: String,
)