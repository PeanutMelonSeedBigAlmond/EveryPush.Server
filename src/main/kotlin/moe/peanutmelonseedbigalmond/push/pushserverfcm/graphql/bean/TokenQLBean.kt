package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class TokenQLBean(
    val uid: Long,
    val token: String,
    val expiredAt: Long,
)