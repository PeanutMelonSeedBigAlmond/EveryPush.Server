package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class PushTokenQLBean(
    val id: Long,
    val token: String,
    val name: String,
    val generatedAt: Long,
)