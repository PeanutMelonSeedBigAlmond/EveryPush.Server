package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class MessageGroupInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L
    lateinit var name: String
    lateinit var groupId: String
    lateinit var uid: String
    var createdAt = 0L

    constructor()

    constructor(name: String, groupId: String, uid: String, createdAt: Long) {
        this.name = name
        this.groupId = groupId
        this.uid = uid
        this.createdAt = createdAt
    }
}