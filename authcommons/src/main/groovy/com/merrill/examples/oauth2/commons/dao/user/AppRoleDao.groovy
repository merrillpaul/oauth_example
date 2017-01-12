package com.merrill.examples.oauth2.commons.dao.user

import com.merrill.examples.oauth2.commons.domain.user.AppRole
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by upaulm2 on 1/12/17.
 */
interface AppRoleDao extends JpaRepository<AppRole, String> {
    AppRole findByAuthority(String authority)

}