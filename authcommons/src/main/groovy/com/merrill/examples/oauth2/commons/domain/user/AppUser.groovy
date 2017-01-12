package com.merrill.examples.oauth2.commons.domain.user

import com.merrill.examples.oauth2.commons.domain.AbstractEntity
import com.merrill.examples.oauth2.commons.jpa.listeners.AppUserEntityListener

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Transient
import java.sql.Timestamp

/**
 * Created by upaulm2 on 1/12/17.
 */
@Entity
@EntityListeners(AppUserEntityListener)
class AppUser extends AbstractEntity {
    @Column(nullable = false, unique = true)
    String username

    @Column(nullable = false)
    String password

    @Column(nullable = false)
    String firstName

    @Column(nullable = false)
    String lastName

    @Column(nullable = false)
    String honorific

    String timeZoneId

    String modifiedBy

    boolean enabled

    boolean accountExpired

    boolean accountLocked

    Timestamp lastUnlocked

    Integer failedLogins

    boolean confirmed

    @ManyToOne
    BusinessEntity businessEntity

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable
            (
                    name = "appUserRoles",
                    joinColumns = [@JoinColumn(name = "userId", referencedColumnName = "id")],
                    inverseJoinColumns = [@JoinColumn(name = "roleId", referencedColumnName = "id")]
            )
    List<AppRole> roles

    @Transient
    def boolean shouldPreventLogin () {
        accountExpired || accountLocked || !enabled
    }
}
