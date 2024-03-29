package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import java.time.Duration
import java.util.*
import javax.persistence.*

/**
 * 登录 token
 */
@Entity
class LoginTokenInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id=0L

    @Column(nullable = false)
    var token:String= UUID.randomUUID().toString().replace("-","")

    @Column(nullable = false)
    var belongsTo:Long=0L

    @Column(nullable = false)
    var expiredAt:Long=0L

    companion object{
        @JvmStatic
        val validation=Duration.ofDays(30).toMillis()
    }
}