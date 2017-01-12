package com.merrill.examples.oauth2.commons.mongo

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/12/17.
 */
@Component
class CustomerTemplateResolver {

    @Autowired
    BeanFactory beanFactory




    MongoTemplate resolve() {
        null
    }
}
