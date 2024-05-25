package moe.peanutmelonseedbigalmond.push.pushserverfcm.utils

object TokenUtils {
    private var chars = ('0'..'9') + ('a'..'z')
    private val nameChars = ('0'..'9') + ('a'..'z') + ('A'..'Z')
    fun randomToken(): String {
        val sb = StringBuffer()
        repeat(32) {
            sb.append(chars.random())
        }
        return sb.toString()
    }

    fun randomKeyName(): String {
        val sb = StringBuffer()
        repeat(6) {
            sb.append(nameChars.random())
        }
        return "Key $sb"
    }
}