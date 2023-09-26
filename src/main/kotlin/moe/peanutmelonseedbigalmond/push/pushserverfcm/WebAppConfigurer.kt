package moe.peanutmelonseedbigalmond.push.pushserverfcm

import moe.peanutmelonseedbigalmond.push.pushserverfcm.component.interceptor.LoginTokenInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebAppConfigurer : WebMvcConfigurer {
    @Bean
    fun getLoginTokenInterceptor(): LoginTokenInterceptor = LoginTokenInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(getLoginTokenInterceptor())
            .excludePathPatterns("/user/login")
            .excludePathPatterns("/ping")
            .excludePathPatterns("/message/push")
        super.addInterceptors(registry)
    }
}