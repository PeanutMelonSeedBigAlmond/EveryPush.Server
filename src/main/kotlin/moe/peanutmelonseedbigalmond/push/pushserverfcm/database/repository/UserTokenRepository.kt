package moe.peanutmelonseedbigalmond.push.pushserverfcm.database.repository

import moe.peanutmelonseedbigalmond.push.pushserverfcm.database.data.UserToken
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findUserTokenByToken(token: String): UserToken?

    @Modifying
    @Transactional
    fun deleteUserTokenByUidAndToken(uid: String, token: String): Long

    @Modifying
    @Transactional
    fun deleteUserTokenByUidAndTokenIsNot(uid: String, token: String): Long

    fun findUserTokensByUid(uid: String, pageable: Pageable? = null): Slice<UserToken>
}