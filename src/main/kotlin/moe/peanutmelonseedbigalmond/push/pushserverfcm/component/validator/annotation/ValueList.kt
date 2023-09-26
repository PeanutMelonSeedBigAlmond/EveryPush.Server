package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.ValueListValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValueListValidator::class])
annotation class ValueList(
    vararg val values:String,
    val message:String="必须为指定的值之一",
    val groups:Array<KClass<*>> = [],
    val payload:Array<KClass<in Payload>> = []
)
