package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class KeyNotExistsException : ApiException(ResponseErrorCode.KeyNotExists, "Key not exists")