package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.ValueListValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValueListValidator::class])
annotation class ValueList(
    vararg val values: String,
    val message: String = "Must be one of the specified value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<in Payload>> = []
)
