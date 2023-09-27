package moe.peanutmelonseedbigalmond.push.pushserverfcm.util

import moe.peanutmelonseedbigalmond.push.pushserverfcm.db.repository.LoginTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import java.util.logging.Logger

@Component
class ClearExpiredToken {
    private val period = Duration.ofHours(1).toMillis()
    private var started = false
    private val timer = Timer("clearExpiredToken")
    private val logger = Logger.getLogger(this::class.java.name)

    @Autowired
    private lateinit var loginTokenRepository: LoginTokenRepository

    fun start() {
        if (started) return
        timer.schedule(object : TimerTask() {
            override fun run() {
                val expiredTokenCount =
                    loginTokenRepository.deleteLoginTokenInfoByExpiredAtBefore(System.currentTimeMillis())
                logger.info("删除了 $expiredTokenCount 条无效 token")
            }
        }, period, period)
        started = true
    }

    fun stop() {
        if (!started) return
        timer.purge()
        timer.cancel()
        started = true
    }
}