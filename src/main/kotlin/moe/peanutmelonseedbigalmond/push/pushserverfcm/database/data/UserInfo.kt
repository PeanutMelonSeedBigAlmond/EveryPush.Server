package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserInfo {
    @Id
    lateinit var uid: String
    var username: String? = null
    var email: String? = null
    var avatarUrl: String? = null
}