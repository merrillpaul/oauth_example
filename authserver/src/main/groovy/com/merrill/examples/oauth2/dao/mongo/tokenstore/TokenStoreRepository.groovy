package com.merrill.examples.oauth2.dao.mongo.tokenstore

import com.merrill.examples.oauth2.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.domain.tokenstore.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by upaulm2 on 1/9/17.
 */
interface AccessTokenRepository extends MongoRepository<AccessToken, String> {

    Long deleteAccessTokenByRefreshToken(String refreshToken)

    AccessToken findByAuthenticationId(String authenticationId)

    List<AccessToken> findByClientId(String clientId)

    List<AccessToken> findByClientIdAndUserName(String clientId, String userName)

}

interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

}

