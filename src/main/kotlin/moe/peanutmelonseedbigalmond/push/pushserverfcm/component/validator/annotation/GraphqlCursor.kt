package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.CursorValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CursorValidator::class])
annotation class GraphqlCursor(
    val message: String = "Bad cursor",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<in Payload>> = []
)