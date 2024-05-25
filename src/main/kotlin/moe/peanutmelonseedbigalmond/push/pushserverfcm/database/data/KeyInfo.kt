package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.*

@Entity
class KeyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L

    lateinit var name: String
    lateinit var uid: String

    @Column(name = "pushKey")
    lateinit var key: String
    var createdAt = 0L

    constructor()

    constructor(name: String, uid: String, key: String, createdAt: Long) {
        this.name = name
        this.uid = uid
        this.key = key
        this.createdAt = createdAt
    }
}