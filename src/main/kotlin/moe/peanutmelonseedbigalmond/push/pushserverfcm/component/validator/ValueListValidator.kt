package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueList
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 验证参数只能取指定的取值
 * @see [ValueList]
 * @property values Array<String>
 */
class ValueListValidator:ConstraintValidator<ValueList,Any> {
    private lateinit var values:Array<String>
    override fun initialize(constraintAnnotation: ValueList) {
        super.initialize(constraintAnnotation)
        values= constraintAnnotation.values as Array<String>
    }
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value is String? && value in values
    }
}