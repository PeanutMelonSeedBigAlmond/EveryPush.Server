package moe.peanutmelonseedbigalmond.push.pushserverfcm.exception

import moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response.ResponseErrorCode

class DeviceNotExistsException : ApiException(ResponseErrorCode.DeviceNotExists, "Device not exists")