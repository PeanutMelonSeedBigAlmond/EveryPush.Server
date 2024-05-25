package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response


data class DeviceInfoResponse(
    val id: Long,
    val uid: String,
    val name: String,
    val platform: String,
    val deviceToken: String,
)