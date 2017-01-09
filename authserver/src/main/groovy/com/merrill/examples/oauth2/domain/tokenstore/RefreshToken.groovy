package com.merrill.examples.oauth2.domain.tokenstore

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by upaulm2 on 1/9/17.
 */
@Document(collection = 'oauth_refresh_token')
class RefreshToken {

    @Id
    String tokenId

    String token

    String authentication
}
