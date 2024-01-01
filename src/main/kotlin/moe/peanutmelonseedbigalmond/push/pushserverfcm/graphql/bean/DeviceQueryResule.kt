package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

data class DeviceItem(
    val id: Long,
    val type: String,
    val deviceId: String,
    val name: String,
)

data class DeviceItemWithCursor(
    val id: Long,
    val type: String,
    val deviceId: String,
    val name: String,
    val cursor: String,
)

data class DeviceQueryResult(
    val pageInfo: QueryPageInfo,
    val items: List<DeviceItemWithCursor>,
)