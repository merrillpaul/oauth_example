package com.merrill.examples.oauth2.endpoint

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import java.security.Principal

/**
 * Created by upaulm2 on 1/11/17.
 */
@FrameworkEndpoint
class LogoutEndpoint {

    @Autowired
    ConsumerTokenServices consumerTokenServices

    @RequestMapping(value = "/oauth/logout_token")
    @ResponseBody
    def logoutToken(Principal principal, @RequestParam("token") String tokenString) {
        consumerTokenServices.revokeToken(tokenString)
    }
}
