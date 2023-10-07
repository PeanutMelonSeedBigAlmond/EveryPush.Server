package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

import javax.validation.constraints.NotBlank

class DeviceRenameParams {
    @NotBlank
    lateinit var token: String
    var id: Long = 0L

    @NotBlank
    lateinit var newName: String
}