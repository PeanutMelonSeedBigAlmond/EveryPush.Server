package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator.annotation.GraphqlCursor
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CursorValidator : ConstraintValidator<GraphqlCursor, String> {
    private lateinit var value: String
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true // 空值表示查询首页，校验通过
        return try {
            val decoded = Base64.getDecoder().decode(value).toString(Charsets.UTF_8)
            decoded.matches(Regex("^cursor(\\d+)$"))
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}