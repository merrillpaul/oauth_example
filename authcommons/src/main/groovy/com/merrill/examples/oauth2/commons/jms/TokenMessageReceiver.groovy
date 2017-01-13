package com.merrill.examples.oauth2.commons.jms

import com.merrill.examples.oauth2.commons.jms.dto.TokenMessagePayload

/**
 * Created by upaulm2 on 1/13/17.
 */
interface TokenMessageReceiver {
    def receiveMessage(TokenMessagePayload tokenMessagePayload)
}