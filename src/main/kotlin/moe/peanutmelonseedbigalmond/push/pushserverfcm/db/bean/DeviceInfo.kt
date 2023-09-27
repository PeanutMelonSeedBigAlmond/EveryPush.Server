package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import javax.persistence.*

@Entity
class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L

    @Column(nullable = false, columnDefinition = "TEXT")
    lateinit var name: String

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    lateinit var fcmToken: String

    @Column(nullable = false)
    var owner = 0L

    @Column(nullable = false, length = 64)
    lateinit var deviceType:String
}