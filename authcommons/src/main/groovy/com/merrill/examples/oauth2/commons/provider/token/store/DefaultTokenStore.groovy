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
import org.springframework.stereotype.Component

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by upaulm2 on 1/9/17.
 */
@Component("appTokenStore")
@Primary
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
                byte[] b = Base64.decoder.decode(authenticationBlobString)
                authentication = deserializeAuthentication(b)
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
        def serBytes = serializeAccessToken(token)
        def tokenBase64String = Base64.encoder.encodeToString(serBytes)
        def authenticationId = authenticationKeyGenerator.extractKey(authentication)
        def username = authentication.isClientOnly() ? null : authentication.name
        def clientId = authentication.getOAuth2Request().getClientId()
        def authenticationBytes = serializeAuthentication(authentication)
        def authenticationBase64String = Base64.encoder.encodeToString(authenticationBytes)
        def refreshTokenKey = extractTokenKey(refreshToken)

        def accessToken = new AccessToken(tokenId: tokenKey, token: tokenBase64String, userName: username,
                clientId: clientId, authenticationId: authenticationId, authentication: authenticationBase64String,
                refreshToken: refreshTokenKey)

        oauthTokenStoreService.saveToken(accessToken)

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
                def tokenBytes = Base64.decoder.decode(accessTokenObj.token)
                accessToken = deserializeAccessToken(tokenBytes)
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
        def tokenSer = Base64.encoder.encodeToString(serializeRefreshToken(refreshToken))
        def authenticationSer = Base64.encoder.encodeToString(serializeAuthentication(authentication))
        def refreshTokenObj = new RefreshToken(tokenId: refreshTokenKey, token: tokenSer, authentication: authenticationSer)
        oauthTokenStoreService.saveToken(refreshTokenObj)
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
                def tokenBytes = Base64.decoder.decode(refreshTokenObj.token)
                refreshToken = deserializeRefreshToken(tokenBytes)
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
                def tokenBytes = Base64.decoder.decode(refreshTokenObj.authentication)
                authentication = deserializeAuthentication(tokenBytes)
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
                def tokenBytes = Base64.decoder.decode(accessTokenObj.token)
                accessToken = deserializeAccessToken(tokenBytes)
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
                def tokenBytes = Base64.decoder.decode(it.token)
                eachObj = deserializeAccessToken(tokenBytes)
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
                def tokenBytes = Base64.decoder.decode(it.token)
                eachObj = deserializeAccessToken(tokenBytes)
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to deserialize access token for " + it.tokenId, e)
            }
            eachObj
        }.findAll {
            it != null
        }

        accessTokens
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null
        }
        MessageDigest digest
        try {
            digest = MessageDigest.getInstance("MD5")
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).")
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"))
            return String.format("%032x", new BigInteger(1, bytes))
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).")
        }
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token)
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token)
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication)
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }
}
