package moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.bean.UserInfo
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserInfo, Long>{
    fun getUserInfoByUid(uid:Long):UserInfo?
    fun getUserInfoByFirebaseUID(firebaseUID:String):UserInfo?
}