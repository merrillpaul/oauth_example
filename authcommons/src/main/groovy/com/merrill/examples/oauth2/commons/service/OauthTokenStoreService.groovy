package com.merrill.examples.oauth2.commons.service

import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken

/**
 * Created by upaulm2 on 1/13/17.
 */
interface OauthTokenStoreService {
    AccessToken getAccessToken(String tokenKey)

    AccessToken saveToken(AccessToken accessToken)

    void deleteAccessToken(String tokenKey)

    RefreshToken saveToken(RefreshToken refreshToken)

    RefreshToken getRefreshToken(String tokenKey)

    void deleteRefreshToken(String tokenKey)

    void deleteAccessTokenWithRefreshToken(String tokenKey)

    AccessToken getAccessTokenForAuthenticationId(String authenticationId)

    List<AccessToken> getAccessTokens(String clientId, String username)

    List<AccessToken> getAccessTokens(String clientId)

}