package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class MessageGroupExistsException : ApiException(ResponseErrorCode.MessageGroupExists, "Message group exists") {
}