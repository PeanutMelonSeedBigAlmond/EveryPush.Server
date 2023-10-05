package moe.peanutmelonseedbigalmond.push.pushserverfcm.util.fcm

/**
 * 数据消息的命令
 * @property command Int
 * @constructor
 */
sealed class FCMMessageCommand(val command: Int) {

    /**
     * 发送通知
     */
    object Notification : FCMMessageCommand(0)
}