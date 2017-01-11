package com.merrill.examples.oauth2.commons.config.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Created by upaulm2 on 1/9/17.
 */
@Configuration
@EnableMongoRepositories(basePackages = 'com.merrill.examples.oauth2.commons.dao.mongo.tokenstore', mongoTemplateRef = "tokenStoreMongoTemplate")
class TokenStoreConfig {

    @Autowired
    EnvironmentAwareMongoDbBuilder environmentAwareMongoDbBuilder

    @Bean(name = "tokenStoreDSConfig")
    @ConfigurationProperties(prefix = "tokenDatasource")
    public def prepareCustomerDataSourceConfigs() {
        [:]
    }


    @Bean(name = "tokenStoreMongoFactory")
    @Primary
    public MongoDbFactory customerMongoFactory() {
        def configs = prepareCustomerDataSourceConfigs()
        environmentAwareMongoDbBuilder.prepareMongoDataSource(configs)
    }

    @Bean(name = "tokenStoreMongoTemplate")
    @Primary
    public MongoTemplate customerMongoTemplate() {
        new MongoTemplate(customerMongoFactory())
    }
}
