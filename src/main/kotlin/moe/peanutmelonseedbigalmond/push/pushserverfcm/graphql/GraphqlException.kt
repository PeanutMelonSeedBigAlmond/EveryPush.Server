package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql

class GraphqlException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}