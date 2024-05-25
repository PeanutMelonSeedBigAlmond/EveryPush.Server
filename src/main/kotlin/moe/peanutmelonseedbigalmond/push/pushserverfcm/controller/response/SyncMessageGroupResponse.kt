package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller.response

data class SyncMessageGroupResponse(
    val deleted: List<Item>,
    val created: List<Item>,
    val renamed: List<Item>,
) {
    data class Item(
        val id: String,
        val name: String,
    )
}