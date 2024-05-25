package moe.peanutmelonseedbigalmond.push.pushserverfcm.data

import moe.peanutmelonseedbigalmond.push.pushserverfcm.data.enumration.FCMCommand

data class FCMPushData<T>(
    val command: FCMCommand,
    val data: T,
)