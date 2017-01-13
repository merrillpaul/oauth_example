package com.merrill.examples.oauth2.commons.provider.token.store

import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.AccessTokenRepository
import com.merrill.examples.oauth2.commons.dao.mongo.tokenstore.RefreshTokenRepository
import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2RefreshToken
import org.springframework.security.oauth2.common.util.SerializationUtils
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import static com.merrill.examples.oauth2.commons.utils.Oauth2SerializationUtils.*
import org.springframework.stereotype.Component

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by upaulm2 on 1/9/17.
 */
class DefaultTokenStore implements TokenStore {

    private static final def LOG = LogFactory.getLog(DefaultTokenStore)

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator()

    @Autowired
    OauthTokenStoreService oauthTokenStoreService

    @Override
    OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        readAuthentication(token.value)
    }

    @Override
    OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication
        def tokenKey = extractTokenKey(token)
        def accessToken = oauthTokenStoreService.getAccessToken(tokenKey)
        try {
            if (accessToken) {
                def authenticationBlobString = accessToken.authentication
                authentication = deserializeAuthentication(authenticationBlobString)
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find access token for token " + token)
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize authentication for " + token, e)
            removeAccessToken(token)
        }
        authentication
    }

    @Override
    void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue()
        }

        if (readAccessToken(token.getValue()) != null) {
            removeAccessToken(token.getValue())
        }

        def tokenKey = extractTokenKey(token.getValue())
        def tokenBase64String = serializeAccessToken(token)
        def authenticationId = authenticationKeyGenerator.extractKey(authentication)
        def username = authentication.isClientOnly() ? null : authentication.name
        def clientId = authentication.getOAuth2Request().getClientId()
        def authenticationBase64String = serializeAuthentication(authentication)
        def refreshTokenKey = extractTokenKey(refreshToken)

        def accessToken = new AccessToken(tokenId: tokenKey, token: tokenBase64String, userName: username,
                clientId: clientId, authenticationId: authenticationId, authentication: authenticationBase64String,
                refreshToken: refreshTokenKey)

        oauthTokenStoreService.saveToken(accessToken, authentication)

    }

    @Override
    OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken

        try {
            def tokenKey = extractTokenKey(tokenValue)
            def accessTokenObj = oauthTokenStoreService.getAccessToken(tokenKey)
            if (!accessTokenObj) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find access token for token " + tokenValue);
                }
            } else {
                accessToken = deserializeAccessToken(accessTokenObj.token)
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize access token for " + tokenValue, e)
            removeAccessToken(tokenValue)
        }

        accessToken
    }

    @Override
    void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.value)
    }

    void removeAccessToken(String token) {
        def tokenKey = extractTokenKey(token)
        oauthTokenStoreService.deleteAccessToken(tokenKey)
    }

    @Override
    void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        def refreshTokenKey = extractTokenKey(refreshToken.value)
        def tokenSer = serializeRefreshToken(refreshToken)
        def authenticationSer = serializeAuthentication(authentication)
        def refreshTokenObj = new RefreshToken(tokenId: refreshTokenKey, token: tokenSer, authentication: authenticationSer)
        oauthTokenStoreService.saveToken(refreshTokenObj, authentication)
    }

    @Override
    OAuth2RefreshToken readRefreshToken(String token) {
        OAuth2RefreshToken refreshToken

        try {
            def tokenKey = extractTokenKey(token)
            def refreshTokenObj = oauthTokenStoreService.getRefreshToken(tokenKey)
            if (!refreshTokenObj) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find refresh token for token " + token)
                }
            } else {
                refreshToken = deserializeRefreshToken(refreshTokenObj.token)
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize refresh token for token " + token, e)
            removeRefreshToken(token);
        }

        refreshToken
    }

    @Override
    OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        OAuth2Authentication authentication
        def value = token.value
        try {
            def tokenKey = extractTokenKey(value)
            def refreshTokenObj = oauthTokenStoreService.getRefreshToken(tokenKey)
            if (!refreshTokenObj) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find refresh token for token " + token)
                }
            } else {
                authentication = deserializeAuthentication(refreshTokenObj.authentication)
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize refresh token for token " + token, e)
            removeRefreshToken(token)
        }
        authentication
    }

    @Override
    void removeRefreshToken(OAuth2RefreshToken token) {
        def tokenKey = extractTokenKey(token.value)
        oauthTokenStoreService.deleteRefreshToken(tokenKey)
    }

    @Override
    void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        def refreshTokenKey = extractTokenKey(refreshToken)
        oauthTokenStoreService.deleteAccessTokenWithRefreshToken(refreshTokenKey)
    }

    @Override
    OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken

        String key = authenticationKeyGenerator.extractKey(authentication)
        try {
            def accessTokenObj = oauthTokenStoreService.getAccessTokenForAuthenticationId(key)
            if (!accessTokenObj) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failed to find access token for authentication " + authentication)
                }
            } else {
                accessToken = deserializeAccessToken(accessTokenObj.token)
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Could not extract access token for authentication " + authentication, e)
        }

        if (accessToken != null
                && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            removeAccessToken(accessToken.getValue())
            // Keep the store consistent (maybe the same user is represented by this authentication but the details have
            // changed)
            storeAccessToken(accessToken, authentication)
        }
        accessToken
    }

    @Override
    Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AccessToken> accessTokens
        def tokens = oauthTokenStoreService.getAccessTokens(clientId, userName)
        accessTokens = tokens.collect {
            def eachObj = null
            try {
                eachObj = deserializeAccessToken(it.token)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize access token for " + it.tokenId, e)
            }
            eachObj
        }.findAll {
            it != null
        }

        accessTokens
    }

    @Override
    Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> accessTokens
        def tokens = oauthTokenStoreService.getAccessTokens(clientId)
        accessTokens = tokens.collect {
            def eachObj = null
            try {
                eachObj = deserializeAccessToken(it.token)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize access token for " + it.tokenId, e)
            }
            eachObj
        }.findAll {
            it != null
        }

        accessTokens
    }


}
