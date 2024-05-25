package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
@ComponentScan
class LoginInterceptorConfigurer : WebMvcConfigurationSupport() {
    @Autowired
    private lateinit var userInfoInterceptor: UserInfoInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userInfoInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/serverInfo/ping")
            .excludePathPatterns("/api/user/login")
            .excludePathPatterns("/api/user/mockLogin")
            .excludePathPatterns("/api/message/push")
        super.addInterceptors(registry)
    }
}