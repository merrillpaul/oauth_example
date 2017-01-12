package com.merrill.examples.oauth2.commons.config.jpa

import com.merrill.examples.oauth2.commons.utils.EnvironmentAwareDataSourceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager

import javax.sql.DataSource

/**
 * Created by upaulm2 on 1/12/17.
 */
@EnableJpaRepositories(
        basePackages = 'com.merrill.examples.oauth2.commons.dao.user',
        entityManagerFactoryRef = 'userEntityManagerFactory',
        transactionManagerRef = 'userTransactionManager'
)
class AppUserRepoConfig {

    @Autowired
    EnvironmentAwareDataSourceBuilder environmentAwareDataSourceBuilder

    @Bean(name = "userDSConfig")
    @ConfigurationProperties(prefix = "userDatasource")
    public def prepareBackofficeDataSourceConfigs() {
        [:]
    }

    @Bean
    public DataSource userDataSource() {
        println "@@@@@@@@@@@Preparing User Datasource"
        environmentAwareDataSourceBuilder.prepareDataSource(prepareBackofficeDataSourceConfigs())
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        builder
                .dataSource(userDataSource())
                .packages('com.merrill.examples.oauth2.commons.domain.user')
                .persistenceUnit("users")
                .build()
    }

    @Bean
    public PlatformTransactionManager userTransactionManager() {
        def transactionManager = new JpaTransactionManager()
        transactionManager.setEntityManagerFactory(userEntityManagerFactory().getObject())
        transactionManager
    }
}
