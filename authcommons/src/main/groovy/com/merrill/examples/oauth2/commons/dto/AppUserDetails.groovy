package com.merrill.examples.oauth2.commons.dto

import com.merrill.examples.oauth2.commons.domain.user.AppUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.firstName
import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.honorific
import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.lastName
import static com.merrill.examples.oauth2.commons.provider.AuthKeysEnum.timezoneId
import static com.merrill.examples.oauth2.commons.provider.AuthUserMetaDataEnum.BUSINESS_ENTITY
import static com.merrill.examples.oauth2.commons.provider.AuthUserMetaDataEnum.COUNTRY_CODE

/**
 * Created by upaulm2 on 1/12/17.
 */
class AppUserDetails extends User {
    static final List NO_ROLES = [new SimpleGrantedAuthority("ROLE_NO_ROLES")]
    def firstName
    def lastName
    def honorific
    def timezoneId
    private final Object id

    boolean passwordExpired
    String accessTicket
    def metaInfo = [:]
    def extraData = [:]

    public AppUserDetails(String id, String username, String firstName, String lastName, String honorific,
                          String timezoneId, def extraData, Collection<? extends GrantedAuthority> authorities) {
        this(id, username, 'NA', firstName, lastName, honorific, timezoneId, extraData, authorities, true, false, false)
    }

    public AppUserDetails(String id, String username, String password, String firstName, String lastName, String honorific,
                          String timezoneId, def extraData, Collection<? extends GrantedAuthority> authorities,
                          boolean enabled, boolean accountExpired, boolean accountLocked) {
        super(username, password, enabled, !accountExpired, true, !accountLocked, authorities?: NO_ROLES)
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.honorific = honorific
        this.timezoneId = timezoneId
        this.passwordExpired = passwordExpired
        this.extraData = extraData?:[:]
    }

   public AppUserDetails(AppUser user, Collection<? extends GrantedAuthority> authorities, boolean passWordExpired) {
       this(user.id, user.username, user.password, user.firstName, user.lastName, user.honorific, user.timeZoneId,
               [(BUSINESS_ENTITY.name()): [
                       id  : user.businessEntity.id,
                       name: user.businessEntity.name
               ],
               (COUNTRY_CODE.name())   : [
                       id  : '11111',
                       name: 'US'
               ]],
               authorities,
               user.enabled,
               user.accountExpired,
               user.accountLocked
       )
       this.metaInfo.tokenStoreUrl = user.businessEntity.tokenStoreUrl
   }

    def getBusinessEntity() {
        getExtraData(BUSINESS_ENTITY.name())
    }

    def getCountryCode() {
        getExtraData(COUNTRY_CODE.name())
    }

    def getExtraData(String key) {
        this.extraData[key]
    }
}
