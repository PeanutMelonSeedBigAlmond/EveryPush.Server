package moe.peanutmelonseedbigalmond.push.pushserverfcm.utils

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserInfo


object ThreadLocalUtil {
    @JvmStatic
    private val userThreadLocal = ThreadLocal<UserInfo>()

    @JvmStatic
    fun addCurrentUser(user: UserInfo) = userThreadLocal.set(user)

    @JvmStatic
    fun getCurrentUser() = userThreadLocal.get()

    @JvmStatic
    fun removeCurrentUser() = userThreadLocal.remove()
}