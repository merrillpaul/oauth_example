package com.merrill.examples.oauth2.commons.service

import com.merrill.examples.oauth2.commons.domain.user.AppUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created by upaulm2 on 10/6/16.
 */
interface UserService extends UserDetailsService {

	UserDetails loadUserByDomainObject(AppUser user) throws UsernameNotFoundException

}