package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import javax.persistence.*

@Entity
class MessageBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var messageId = 0L

    @Column(nullable = false)
    var title: String? = null

    @Column(columnDefinition = "MEDIUMTEXT")
    lateinit var text: String

    @Column(nullable = false)
    lateinit var type: String

    @Column(nullable = false)
    var pushTime: Long = 0L

    @Column(nullable = false)
    var owner = 0L

    @Column(nullable = false)
    var deleted = false
}