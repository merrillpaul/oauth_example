package com.merrill.examples.oauth2.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler
import org.springframework.security.oauth2.provider.token.TokenStore

/**
 * Created by upaulm2 on 1/10/17.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OAuth2ResourceServerConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    @Qualifier("appTokenStore")
    private TokenStore tokenStore

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        new OAuth2MethodSecurityExpressionHandler()
    }

}


/*
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        new OAuth2MethodSecurityExpressionHandler()
    }
}
*/
