package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import javax.persistence.*

@Entity
class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var uid = 0L

    @Column(nullable = false)
    lateinit var username: String

    @Column(unique = true, nullable = false)
    lateinit var firebaseUID: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfo

        if (username != other.username) return false
        return firebaseUID == other.firebaseUID
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + firebaseUID.hashCode()
        return result
    }
}