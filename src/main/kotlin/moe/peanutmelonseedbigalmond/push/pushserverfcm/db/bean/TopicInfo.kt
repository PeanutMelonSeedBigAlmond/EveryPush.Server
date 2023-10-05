package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
class TopicInfo {
    @EmbeddedId
    lateinit var pk: TopicInfoPK

    @Column(nullable = false)
    lateinit var name: String

    /**
     * 复合主键
     * @property owner Long
     * @property topicId String
     */
    @Embeddable
    class TopicInfoPK() : Serializable {
        companion object {
            private const val serialVersionUID = 15676418976432489L
        }

        @Column(nullable = false)
        var owner: Long = 0L

        @Column(nullable = false)
        lateinit var topicId: String

        constructor(owner: Long, topicId: String) : this() {
            this.owner = owner
            this.topicId = topicId
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopicInfoPK

            if (owner != other.owner) return false
            return topicId == other.topicId
        }

        override fun hashCode(): Int {
            var result = owner.hashCode()
            result = 31 * result + topicId.hashCode()
            return result
        }
    }
}