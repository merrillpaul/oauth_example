package com.merrill.examples.oauth2.commons.jms

import com.merrill.examples.oauth2.commons.dao.user.AppUserDao
import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken
import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import com.merrill.examples.oauth2.commons.jms.dto.TokenMessagePayload
import com.merrill.examples.oauth2.commons.jms.dto.TokenMessageType
import com.merrill.examples.oauth2.commons.service.CurrentUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/12/17.
 */
class CustomerLocationTokenStorePublisher {

    @Autowired(required = false)
    JmsTemplate jmsTemplate

    @Autowired
    AppUserDao appUserDao

    def saveAccessToken(AccessToken accessToken, def principal) {
        jmsTemplate.convertAndSend(TokenMessagePayload.DESTINATION, new TokenMessagePayload(
                tokenStoreCreds: resolveUserLocationTokenStoreCreds(principal),
                type: TokenMessageType.SAVE_ACCESS_TOKEN, payload: accessToken))
    }

    def saveRefreshToken(RefreshToken refreshToken, def principal) {
        jmsTemplate.convertAndSend(TokenMessagePayload.DESTINATION, new TokenMessagePayload(
                tokenStoreCreds: resolveUserLocationTokenStoreCreds(principal),
                type: TokenMessageType.SAVE_REFRESH_TOKEN, payload: refreshToken))
    }

    def deleteAccessToken(AccessToken accessToken, def principal) {
        jmsTemplate.convertAndSend(TokenMessagePayload.DESTINATION, new TokenMessagePayload(
                tokenStoreCreds: resolveUserLocationTokenStoreCreds(principal),
                type: TokenMessageType.DEL_ACCESS_TOKEN, payload: accessToken.tokenId))
    }

    def deleteRefreshToken(RefreshToken refreshToken, def principal) {
        jmsTemplate.convertAndSend(TokenMessagePayload.DESTINATION, new TokenMessagePayload(
                tokenStoreCreds: resolveUserLocationTokenStoreCreds(null),
                type: TokenMessageType.DEL_REFRESH_TOKEN, payload: refreshToken.tokenId))
    }

    def deleteAccessTokenByRefreshToken(RefreshToken refreshToken, def principal) {
        jmsTemplate.convertAndSend(TokenMessagePayload.DESTINATION, new TokenMessagePayload(
                tokenStoreCreds: resolveUserLocationTokenStoreCreds(),
                type: TokenMessageType.DEL_ACCESS_BY_REFRESH_TOKEN, payload: refreshToken.tokenId))
    }

    private resolveUserLocationTokenStoreCreds(principal) {
        def tokenStoreUrl
        def result
        if (principal && principal instanceof AppUserDetails) {
            def userDetails = (AppUserDetails) principal
            def appUser = appUserDao.findOne(userDetails.id)
            result = [
                    tokenStoreUrl: appUser.businessEntity.tokenStoreUrl,
                    tokenStoreLocationKey: appUser.businessEntity.name
            ]
        }
        result
    }
}
