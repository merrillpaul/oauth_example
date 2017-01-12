package com.merrill.examples.oauth2.commons.service.impl

import com.merrill.examples.oauth2.commons.dao.user.AppUserDao
import com.merrill.examples.oauth2.commons.domain.user.AppUser
import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import com.merrill.examples.oauth2.commons.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Primary
@Transactional
class AppUserDetailsService implements  UserService {
    @Autowired
    AppUserDao appUserDao

    @Autowired
    PasswordEncoder passwordEncoder

    @Override
    @SuppressWarnings("UnusedMethodParameter")
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserDao.findByUsername(username)
        if (!user) throw new UsernameNotFoundException("User ${username} not found")

        def roles = user.roles ?: []
        def springRoles /* haha , not egg rolls*/ = roles.collect {
            new SimpleGrantedAuthority(it.authority)
        }
        UserDetails userDetails = new AppUserDetails(user, springRoles, isPasswordExpired(user))

        userDetails
    }

    @Override
    @SuppressWarnings("UnusedMethodParameter")
    @Transactional(readOnly = true)
    public UserDetails loadUserByDomainObject(AppUser user) throws UsernameNotFoundException {
        loadUserByUsername(user.username)
    }

    def isPasswordExpired(AppUser user) {
        // TODO implement logic to check whether its overrage
        false
    }
}