package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class MessageGroupNotExistsException :
    ApiException(ResponseErrorCode.MessageGroupNotExists, "Message group not exists")