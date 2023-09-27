package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import javax.persistence.*

@Entity
class PushTokenInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false, unique = true)
    lateinit var pushToken: String

    @Column(nullable = false)
    var owner = 0L

    @Column(nullable = false)
    var generatedAt=0L
}