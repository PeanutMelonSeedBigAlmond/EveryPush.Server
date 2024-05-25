package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class UserNotExistsException : ApiException(ResponseErrorCode.UserNotExists, "User not exists") {
}