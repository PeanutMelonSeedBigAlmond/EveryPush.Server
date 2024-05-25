package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data

import jakarta.persistence.*
import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.MessageType

@Entity
class MessageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L
    var title: String? = null

    @Column(columnDefinition = "TEXT")
    lateinit var content: String

    @Enumerated(EnumType.STRING)
    lateinit var type: MessageType
    var priority = 0
    lateinit var uid: String
    var coverImgUrl: String? = null
    var messageGroupId: String? = null
    var encrypted = false
    var pushedAt: Long = 0L

    constructor()
    constructor(
        title: String?,
        content: String,
        type: MessageType,
        priority: Int,
        uid: String,
        coverImgUrl: String?,
        messageGroupId: String?,
        encrypted: Boolean,
        pushedAt: Long
    ) {
        this.title = title
        this.content = content
        this.type = type
        this.priority = priority
        this.uid = uid
        this.coverImgUrl = coverImgUrl
        this.messageGroupId = messageGroupId
        this.encrypted = encrypted
        this.pushedAt = pushedAt
    }
}