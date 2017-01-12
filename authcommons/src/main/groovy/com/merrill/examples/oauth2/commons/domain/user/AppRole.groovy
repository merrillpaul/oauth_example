package com.merrill.examples.oauth2.commons.domain.user

import com.merrill.examples.oauth2.commons.domain.AbstractEntity

import javax.persistence.Column
import javax.persistence.Entity

/**
 * Created by upaulm2 on 1/12/17.
 */
@Entity
/*@NamedQuery(name = "AppRole.findByAuthority",
		query = "select ar from AppRole ar where ar.authority = ?1")*/
class AppRole extends AbstractEntity {

    @Column(nullable = false, unique = true)
    String authority

    public String toString() {
        authority
    }
}
