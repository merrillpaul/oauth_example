package com.merrill.examples.oauth2.listeners

import com.merrill.examples.oauth2.commons.dao.user.AppRoleDao
import com.merrill.examples.oauth2.commons.dao.user.AppUserDao
import com.merrill.examples.oauth2.commons.dao.user.BusinessEntityDao
import com.merrill.examples.oauth2.commons.domain.user.AppRole
import com.merrill.examples.oauth2.commons.domain.user.BusinessEntity
import com.merrill.examples.oauth2.commons.domain.user.AppUser
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import java.sql.Timestamp
import java.time.LocalDateTime

/**
 * Created by upaulm2 on 1/12/17.
 */
@Component
class DevDatabasePopulator implements ApplicationListener<ContextStartedEvent>, BeanFactoryAware {

    @Autowired
    Environment environment

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    AppRoleDao appRoleDao

    @Autowired
    AppUserDao appUserDao

    @Autowired
    BusinessEntityDao businessEntityDao

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    ResourceLoader resourceLoader

    private ConfigurableListableBeanFactory beanFactory

    @Override
    void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory
    }

    @Override
    void onApplicationEvent(ContextStartedEvent event) {

        if (environment.getActiveProfiles().contains('dev')) {
            println "@@@@Populating dev database"

            // adding roles
            [
                    "ROLE_SYSTEM_ADMIN",
                    "ROLE_CLINICIAN",
                    "ROLE_CUSTOMER_SERVICE",
                    "ROLE_FINANCE",
                    "ROLE_NO_ROLES"
            ].each {
                appRoleDao.save(new AppRole(authority: it, lastCreated: Timestamp.valueOf(LocalDateTime.now())))
            }

            def entities = []

            [
                    [name: "US", tokenStore: "mongodb://tokenstore_user:Password1!@localhost:27017/tokenstoredb_US"],
                    [name: "FR", tokenStore: "mongodb://tokenstore_user:Password1!@localhost:27017/tokenstoredb_FR"]

            ].each {
                entities << businessEntityDao.save(new BusinessEntity(name: it.name, tokenStoreUrl: it.tokenStore))
            }



            def roles = [
                    "ROLE_SYSTEM_ADMIN",
                    "ROLE_CLINICIAN"
            ].collect {
                appRoleDao.findByAuthority(it)
            }

            def user = appUserDao.save(new AppUser(username: 'jon_us', password: 'Password1', firstName: "Merrill",
                    lastName: 'Paul', timeZoneId: TimeZone.default.ID, honorific: 'Mr', businessEntity: entities[0],
                    accountExpired: false, accountLocked: false, lastCreated: Timestamp.valueOf(LocalDateTime.now()),
                    enabled: true))
            user.roles = roles
            appUserDao.save(user)


            user = appUserDao.save(new AppUser(username: 'jon_fr', password: 'Password1', firstName: "Merrill",
                    lastName: 'Paul', timeZoneId: TimeZone.default.ID, honorific: 'Mr', businessEntity: entities[1],
                    accountExpired: false, accountLocked: false, lastCreated: Timestamp.valueOf(LocalDateTime.now()),
                    enabled: true))
            user.roles = roles
            appUserDao.save(user)

        }

    }
}
