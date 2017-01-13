package com.merrill.examples.oauth2.commons.service.impl

import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.AccessTokenRepository
import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.RefreshTokenRepository
import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken
import com.merrill.examples.oauth2.commons.jms.CustomerLocationTokenStorePublisher
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import com.merrill.examples.oauth2.commons.utils.Oauth2SerializationUtils
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/13/17.
 */
class MongoOauthTokenStoreService implements  OauthTokenStoreService {

    @Value('${config.auth_server}')
    boolean authServerMode

    @Autowired
    AccessTokenRepository accessTokenRepository

    @Autowired
    RefreshTokenRepository refreshTokenRepository

    @Autowired
    CustomerLocationTokenStorePublisher customerLocationTokenStorePublisher

    private static final def LOG = LogFactory.getLog(MongoOauthTokenStoreService)

    @Override
    AccessToken getAccessToken(String tokenKey) {
        return accessTokenRepository.findOne(tokenKey)
    }

    @Override
    AccessToken saveToken(AccessToken accessToken, OAuth2Authentication authentication) {
        def token = accessTokenRepository.save(accessToken)
        if(authServerMode) {
            customerLocationTokenStorePublisher.saveAccessToken(accessToken, authentication.principal)
        }
        token
    }

    @Override
    void deleteAccessToken(String tokenKey) {
        def accessToken = getAccessToken(tokenKey)
        accessTokenRepository.delete(tokenKey)
        if(authServerMode && accessToken) {
            try {
                def authentication = Oauth2SerializationUtils.deserializeAuthentication(accessToken.authentication)
                customerLocationTokenStorePublisher.deleteAccessToken(accessToken, authentication.principal)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize authentication from access token for token " + tokenKey)
               // ignored
            }
        }
    }

    @Override
    RefreshToken saveToken(RefreshToken refreshToken, OAuth2Authentication authentication) {
        def token = refreshTokenRepository.save(refreshToken)
        if(authServerMode) {
            customerLocationTokenStorePublisher.saveRefreshToken(refreshToken, authentication.principal)
        }
        token
    }

    @Override
    RefreshToken getRefreshToken(String tokenKey) {
        return refreshTokenRepository.findOne(tokenKey)
    }

    @Override
    void deleteRefreshToken(String tokenKey) {
        def refreshToken = getRefreshToken(tokenKey)
        refreshTokenRepository.delete(tokenKey)
        if(authServerMode && refreshToken) {
            try {
                def authentication = Oauth2SerializationUtils.deserializeAuthentication(refreshToken.authentication)
                customerLocationTokenStorePublisher.deleteRefreshToken(refreshToken, authentication.principal)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize authentication from access token for token " + tokenKey)
                // ignored
            }
        }
    }

    @Override
    void deleteAccessTokenWithRefreshToken(String tokenKey) {
        def refreshToken = getRefreshToken(tokenKey)
        accessTokenRepository.deleteAccessTokenByRefreshToken(tokenKey)
        if(authServerMode && refreshToken) {
            try {
                def authentication = Oauth2SerializationUtils.deserializeAuthentication(refreshToken.authentication)
                customerLocationTokenStorePublisher.deleteAccessTokenByRefreshToken(refreshToken, authentication.principal)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize authentication from access token for token " + tokenKey)
                // ignored
            }
        }
    }

    @Override
    AccessToken getAccessTokenForAuthenticationId(String authenticationId) {
        return accessTokenRepository.findByAuthenticationId(authenticationId)
    }

    @Override
    List<AccessToken> getAccessTokens(String clientId, String username) {
        return accessTokenRepository.findByClientIdAndUserName(clientId, username)
    }

    @Override
    List<AccessToken> getAccessTokens(String clientId) {
        return accessTokenRepository.findByClientId(clientId)
    }
}
