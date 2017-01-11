package com.merrill.examples.oauth2.commons.domain.tokenstore

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by upaulm2 on 1/9/17.
 */
@Document(collection = 'oauth_access_token')
public class AccessToken {

    @Id
    String tokenId

    /* a base64 encoded string for ser-deser OAuth2Authentication object*/
    String token

    String authenticationId

    String userName

    String clientId

    /* a base64 encoded string for ser-deser OAuth2Authentication object*/
    String authentication

    String refreshToken

}
