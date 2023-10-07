package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.ValueListOrNull
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValueListOrNullValidator : ConstraintValidator<ValueListOrNull, Any> {
    private lateinit var values: Array<String>
    override fun initialize(constraintAnnotation: ValueListOrNull) {
        super.initialize(constraintAnnotation)
        values = constraintAnnotation.values as Array<String>
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value is String? && (value == null || value in values)
    }
}