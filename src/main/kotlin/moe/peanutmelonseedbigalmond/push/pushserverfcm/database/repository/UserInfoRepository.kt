package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserInfo
import org.springframework.data.repository.CrudRepository

interface UserInfoRepository : CrudRepository<UserInfo, String> {
}