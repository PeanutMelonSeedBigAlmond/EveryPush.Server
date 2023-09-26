package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class LoginResponse(
    val token:String,
    val expiredAt:Long,
)