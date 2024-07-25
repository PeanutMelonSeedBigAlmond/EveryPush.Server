package moe.peanutmelonseedbigalmond.push.pushserverfcm.utils

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserInfo
import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserToken


object ThreadLocalUtil {
    @JvmStatic
    private val userThreadLocal = ThreadLocal<UserInfo>()

    @JvmStatic
    fun addCurrentUser(user: UserInfo) = userThreadLocal.set(user)

    @JvmStatic
    fun getCurrentUser() = userThreadLocal.get()

    @JvmStatic
    fun removeCurrentUser() = userThreadLocal.remove()


    @JvmStatic
    private val userTokenThreadLocal = ThreadLocal<UserToken>()

    @JvmStatic
    fun addCurrentUserToken(userToken: UserToken) = userTokenThreadLocal.set(userToken)

    @JvmStatic
    fun getCurrentUserToken() = userTokenThreadLocal.get()

    @JvmStatic
    fun removeCurrentUserToken() = userTokenThreadLocal.remove()
}