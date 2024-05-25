package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class ResponseWrapper<T>(
    val data: T? = null,
    val errorCode: ResponseErrorCode = ResponseErrorCode.Ok,
    val message: String? = null,
) {
    companion object {
        fun <T> successWith(data: T): ResponseWrapper<T> {
            return ResponseWrapper(data, ResponseErrorCode.Ok, null)
        }

        fun failed(errorCode: ResponseErrorCode, message: String?): ResponseWrapper<Any?> {
            return ResponseWrapper(null, errorCode, message)
        }
    }
}