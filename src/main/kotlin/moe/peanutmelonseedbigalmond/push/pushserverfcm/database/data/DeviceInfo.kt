package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L
    lateinit var uid: String
    lateinit var name: String
    lateinit var platform: String
    lateinit var deviceToken: String

    constructor()

    constructor(uid: String, name: String, platform: String, deviceToken: String) {
        this.uid = uid
        this.name = name
        this.platform = platform
        this.deviceToken = deviceToken
    }
}