package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class UserInfoResponse(
    val uid: String,
    val name: String?,
    val email: String?,
    val avatarUrl: String?,
)