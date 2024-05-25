package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

@JvmInline
value class ResponseErrorCode private constructor(
    val message: String,
) {
    companion object {
        val Ok = ResponseErrorCode("00000")
        val InternalError = ResponseErrorCode("B0001")
        val InvalidCredentialError = ResponseErrorCode("A0210")
        val InvalidUserLoginToken = ResponseErrorCode("A0220")
        val UserNotExists = ResponseErrorCode("A0201")
        val DeviceNotExists = ResponseErrorCode("A0606")
        val KeyNotExists = ResponseErrorCode("A0607")
        val MessageGroupExists = ResponseErrorCode("A0608")
        val MessageGroupNotExists = ResponseErrorCode("A0609")
        val MessageNotExists = ResponseErrorCode("A0610")
        val MalformedParameters = ResponseErrorCode("A0421")
        val MessageServiceError = ResponseErrorCode("C0120")
    }
}