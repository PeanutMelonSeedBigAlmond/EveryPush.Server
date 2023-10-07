package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueListOrNull
import javax.validation.constraints.NotBlank

class PushMessageParams {
    @NotBlank
    lateinit var pushToken: String

    @NotBlank
    lateinit var text: String
    var title: String? = null
        get() = if (field == null) "" else field

    @ValueListOrNull(values = ["text", "image", "markdown"])
    var type: String? = null
        get() = if (field == null) "text" else field
    val topicId: String? = null
}