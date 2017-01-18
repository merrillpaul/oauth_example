package com.merrill.examples.oauth2.commons.service.impl

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.RemoteTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore

/**
 * Checks if the token is available in the local db, if not tries to
 * resolve it from the auth server and caches it locally.
 *
 *
 * 1. loads from local ds
 * 2. checks whether expired, if expired throws exception
 * 3. if not found tries to resolve from the remote auth server
 * 4. if token successful then caches into local ds
 */
class LocalAwareRemoteTokenServices extends RemoteTokenServices {

    @Autowired
    TokenStore tokenStore // represents the local token store

    protected final Log LOGGER = LogFactory.getLog(getClass())


    @Override
    OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        OAuth2AccessToken accessToken
        OAuth2Authentication authentication
        try {
            accessToken = tokenStore.readAccessToken(accessTokenValue)
            if (accessToken == null) {
                throw new InvalidTokenException("Invalid access token: " + accessTokenValue)
            } else if (accessToken.isExpired()) {
                tokenStore.removeAccessToken(accessToken)
                throw new InvalidTokenException("Access token expired: " + accessTokenValue)
            }

            authentication = tokenStore.readAuthentication(accessToken)
            if (authentication == null) {
                // in case of race condition
                throw new InvalidTokenException("Invalid access token: " + accessTokenValue)
            }
        } catch (InvalidTokenException te) {
            LOGGER.warn("invalid token in local token store") // not to worry and log exception and scare everybody
            // just to handle the error and then let the remote check_token to be fired as a fallback

        }

        if (!authentication) {
            // try to get it from remote
            authentication = super.loadAuthentication(accessTokenValue)
        }
        authentication
    }
}
