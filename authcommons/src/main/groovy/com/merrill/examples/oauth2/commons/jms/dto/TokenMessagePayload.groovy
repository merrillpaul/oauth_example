package com.merrill.examples.oauth2.commons.jms.dto

/**
 * Created by upaulm2 on 1/13/17.
 */
class TokenMessagePayload {
    TokenMessageType type

    Object payload

    def tokenStoreCreds

    public static final DESTINATION = "tokenstore"
}
