package com.merrill.examples.oauth2.commons.provider.token.converter

import com.merrill.examples.oauth2.commons.dao.user.AppUserDao
import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.*
import static com.merrill.examples.oauth2.commons.provider.AuthUserMetaDataEnum.*

/**
 * Created by upaulm2 on 1/18/17.
 * Adds extra attributes that might be needed in the API ( customer centric application) to get extra information about
 * the principal/user
 */
class AuthServerAccessTokenConverter extends DefaultAccessTokenConverter {

    @Autowired
    AppUserDao userDao


    @Override
    Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {

        def resp =  super.convertAccessToken(token, authentication)
        resp.putAll(addExtraAttributes(token, authentication))
        resp
    }

    protected Map<String, ?> addExtraAttributes(OAuth2AccessToken token, OAuth2Authentication authentication) {
        def principal = authentication.principal
        if (principal instanceof AppUserDetails) {
            def user = userDao.findByUsername(principal.username)
            def extra = [
                    (id.name()): principal.id,
                    (firstName.name()) : principal.firstName,
                    (lastName.name()): principal.lastName,
                    (timezoneId.name()): principal.timezoneId,
                    (honorific.name()): principal.honorific,
                    metaData: [
                            (BUSINESS_ENTITY.name()): [
                                    id: user.businessEntity.id,
                                    name: user.businessEntity.name
                            ],
                            (COUNTRY_CODE.name()): [
                                    id: '11111',
                                    name: 'US'
                            ]
                    ]
            ]

        }
    }
}
