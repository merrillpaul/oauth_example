package com.merrill.examples.oauth2.config.mongo

import com.mongodb.MongoClientURI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/9/17.
 */
@Component
class EnvironmentAwareMongoDbBuilder {

    @Autowired
    Environment environment


    def MongoDbFactory prepareMongoDataSource(def config) {
        def factory
        factory = new SimpleMongoDbFactory(new MongoClientURI(config['mongo-uri']))

        if (environment.activeProfiles.contains('dev')) {

            factory.db.getCollectionNames().each {
                println "Dropping Mongo Collection ${it} in dev mode"
                factory.db.getCollection(it).drop()
            }

        }
        factory
    }
}
