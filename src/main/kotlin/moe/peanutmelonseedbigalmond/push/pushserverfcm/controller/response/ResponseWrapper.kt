package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class ResponseWrapper<Response>(
    val message: String = "",
    val data: Response?=null,
)