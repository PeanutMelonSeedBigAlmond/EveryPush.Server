package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

import javax.validation.constraints.NotBlank

class DeviceRegisterParams {
    @NotBlank
    lateinit var token: String

    @NotBlank
    lateinit var deviceId: String
    var type: String? = null
        get() = field ?: "android"

    @NotBlank
    lateinit var name: String
}