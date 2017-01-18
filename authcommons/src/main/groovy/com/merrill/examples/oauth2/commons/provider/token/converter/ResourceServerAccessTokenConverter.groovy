package com.merrill.examples.oauth2.commons.provider.token.converter

import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.*
import static com.merrill.examples.oauth2.commons.provider.AuthUserMetaDataEnum.*

/**
 * This unifies the principal into AppUserDetails if for some reason
 * the local token store didnt have the information
 */
class ResourceServerAccessTokenConverter extends DefaultAccessTokenConverter {
    @Override
    OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        def authentication = super.extractAuthentication(map)
        if (!(authentication.principal instanceof AppUserDetails)) {
            def user = new AppUserDetails(map[id.name()], authentication.principal, map[firstName.name()],
                    map[lastName.name()], map[honorific.name()], map[timezoneId.name()], map.metaData ?: [:], authentication.authorities)
            authentication = new OAuth2Authentication(authentication.getOAuth2Request(),
                    new UsernamePasswordAuthenticationToken(user, "N/A", authentication.authorities))
        }
        authentication
    }
}
