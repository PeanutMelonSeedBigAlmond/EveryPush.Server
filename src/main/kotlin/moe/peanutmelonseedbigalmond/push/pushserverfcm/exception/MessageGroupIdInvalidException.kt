package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class MessageGroupIdInvalidException : ApiException(ResponseErrorCode.MessageGroupIdInvalid, "Invalid message group id")