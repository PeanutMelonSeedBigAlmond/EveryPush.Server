package moe.peanutmelonseedbigalmond.push.pushserverfcm.controller

data class GraphqlResponseAdapter<T>(
    val data: T? = null,
    val errors: List<GraphqlErrorAdapter>? = null
) {
    constructor(errorMessage: String) : this(null, listOf(GraphqlErrorAdapter(errorMessage)))
    constructor(data: T?) : this(data, null)
}