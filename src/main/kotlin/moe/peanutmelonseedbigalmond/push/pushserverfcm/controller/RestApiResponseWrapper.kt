package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

data class RestApiResponseWrapper<T>(
    val data: T? = null,
    val message: String? = null,
)