package com.merrill.examples.oauth2.commons.dao.user

import com.merrill.examples.oauth2.commons.domain.user.AppUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by upaulm2 on 1/12/17.
 */
interface AppUserDao extends JpaRepository<AppUser, String>{
    AppUser findByUsername(String username)
}