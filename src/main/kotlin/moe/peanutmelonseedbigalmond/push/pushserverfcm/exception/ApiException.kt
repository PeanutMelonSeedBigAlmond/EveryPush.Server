package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

open class ApiException(val errorCode: ResponseErrorCode, val errorMessage: String? = null) :
    Exception("error=$errorCode, message=$errorMessage")