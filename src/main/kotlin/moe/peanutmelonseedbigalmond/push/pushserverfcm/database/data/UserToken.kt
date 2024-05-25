package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.*

@Entity
class UserToken {
    constructor()
    constructor(
        token: String,
        uid: String,
        createdAt: Long,
        updatedAt: Long,
        deviceName: String,
        devicePlatform: String,
        isMockLogin: Boolean
    ) {
        this.token = token
        this.uid = uid
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.deviceName = deviceName
        this.devicePlatform = devicePlatform
        this.isMockLogin = isMockLogin
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L

    @Column(unique = true, nullable = false)
    lateinit var token: String

    lateinit var uid: String

    var createdAt = 0L

    var updatedAt = 0L

    lateinit var deviceName: String

    lateinit var devicePlatform: String

    var isMockLogin = false
}