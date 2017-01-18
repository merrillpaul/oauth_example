package com.merrill.examples.oauth2.config

import com.merrill.examples.oauth2.commons.service.impl.LocalAwareRemoteTokenServices
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

/**
 * Created by upaulm2 on 1/10/17.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OAuth2ResourceServerConfig extends GlobalMethodSecurityConfiguration {

    @Value('${config.oauth2.check_token_url}')
    String checkTokenUrl

    @Value('${config.oauth2.client_id}')
    String checkTokenClient

    @Value('${config.oauth2.client_secret}')
    String checkTokenClientSecret

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        new OAuth2MethodSecurityExpressionHandler()
    }

    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        def tokenServices = new LocalAwareRemoteTokenServices()
        tokenServices.checkTokenEndpointUrl = checkTokenUrl
        tokenServices.clientId = checkTokenClient
        tokenServices.clientSecret = checkTokenClientSecret
        tokenServices
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
