package moe.peanutmelonseedbigalmond.push.pushserverfcm.component.validator

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.validation.MessageInterpolatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Component
class MyParameterValidator {
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun defaultValidator(): LocalValidatorFactoryBean {
        val factoryBean = CustomLocalValidatorFactoryBean()
        factoryBean.messageInterpolator = MessageInterpolatorFactory().getObject()
        return factoryBean
    }
}