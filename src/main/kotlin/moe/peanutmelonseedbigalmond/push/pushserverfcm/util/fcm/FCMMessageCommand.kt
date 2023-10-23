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

    /**
     * 添加 topic
     */
    object AddTopic : FCMMessageCommand(1)

    /**
     * 删除 topic
     */
    object DeleteTopic : FCMMessageCommand(1)
}