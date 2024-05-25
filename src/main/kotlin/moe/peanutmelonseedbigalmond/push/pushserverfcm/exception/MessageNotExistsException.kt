package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class MessageNotExistsException : ApiException(ResponseErrorCode.MessageNotExists, "Message not exists")