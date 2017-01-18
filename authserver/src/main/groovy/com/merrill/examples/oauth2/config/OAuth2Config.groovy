package com.merrill.examples.oauth2.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

/**
 * Created by upaulm2 on 1/3/17.
 */
@Configuration
@EnableAuthorizationServer
class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager

    @Autowired
    private TokenStore tokenStore

    @Autowired
    private AccessTokenConverter accessTokenConverter

    @Override
    public void configure(
            AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {

        oauthServer.realm(OAuthConfigEnum.REALM.code)
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("acme")
                .secret("acmesecret")
                .authorizedGrantTypes("authorization_code", "refresh_token",
                "password", "implicit").scopes("read")
                .and()
                .withClient("sampleClientId")
                .authorizedGrantTypes("implicit")
                .scopes("read")
                .autoApprove(true)
                .and()
                .withClient("checkTokenClient")
                .secret("password1")
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter)
    }


}
