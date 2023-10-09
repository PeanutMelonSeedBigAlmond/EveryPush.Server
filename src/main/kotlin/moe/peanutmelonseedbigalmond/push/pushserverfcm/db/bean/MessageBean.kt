package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import javax.persistence.*

@Entity
class MessageBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var messageId = 0L

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    lateinit var title: String

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    lateinit var text: String

    @Column(nullable = false)
    lateinit var type: String

    @Column(nullable = false)
    var pushTime: Long = 0L

    @Column(nullable = false)
    var owner = 0L

    @Column(nullable = true, columnDefinition = "varchar(255) default null")
    var topicId: String? = null

    @Column(nullable = false)
    var deleted = false
}