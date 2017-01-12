package com.merrill.examples.oauth2.commons.dto

import com.merrill.examples.oauth2.commons.domain.user.AppUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

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

   public AppUserDetails(AppUser user, Collection<? extends GrantedAuthority> authorities, boolean passWordExpired) {
       super(user.username, user.password, user.enabled, !user.accountExpired, true, !user.accountLocked, authorities?: NO_ROLES)
       this.id = user.id
       this.firstName = user.firstName
       this.lastName = user.lastName
       this.honorific = user.honorific
       this.timezoneId = user.timeZoneId
       this.passwordExpired = passwordExpired
       this.metaInfo.tokenStoreUrl = user.businessEntity.tokenStoreUrl
   }
}
