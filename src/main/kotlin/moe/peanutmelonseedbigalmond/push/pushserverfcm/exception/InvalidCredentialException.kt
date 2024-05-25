package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

/**
 * Firebase token 错误
 */
class InvalidCredentialException : ApiException(ResponseErrorCode.InvalidCredentialError, "Invalid credential")