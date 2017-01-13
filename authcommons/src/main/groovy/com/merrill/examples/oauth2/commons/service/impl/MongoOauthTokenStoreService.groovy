package com.merrill.examples.oauth2.commons.service.impl

import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.AccessTokenRepository
import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.RefreshTokenRepository
import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/13/17.
 */
@Component("mongoTokenStoreService")
@Primary
class MongoOauthTokenStoreService implements  OauthTokenStoreService {

    @Autowired
    AccessTokenRepository accessTokenRepository

    @Autowired
    RefreshTokenRepository refreshTokenRepository

    @Override
    AccessToken getAccessToken(String tokenKey) {
        return accessTokenRepository.findOne(tokenKey)
    }

    @Override
    AccessToken saveToken(AccessToken accessToken) {
        return accessTokenRepository.save(accessToken)
    }

    @Override
    void deleteAccessToken(String tokenKey) {
        accessTokenRepository.delete(tokenKey)
    }

    @Override
    RefreshToken saveToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken)
    }

    @Override
    RefreshToken getRefreshToken(String tokenKey) {
        return refreshTokenRepository.findOne(tokenKey)
    }

    @Override
    void deleteRefreshToken(String tokenKey) {
        refreshTokenRepository.delete(tokenKey)
    }

    @Override
    void deleteAccessTokenWithRefreshToken(String tokenKey) {
        accessTokenRepository.deleteAccessTokenByRefreshToken(tokenKey)
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
