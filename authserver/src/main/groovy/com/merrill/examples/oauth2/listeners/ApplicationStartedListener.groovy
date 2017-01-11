package com.merrill.examples.oauth2.listeners

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/11/17.
 */
@Component
class ApplicationStartedListener implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    Environment environment

    @Autowired
    ApplicationContext applicationContext

    @Override
    void onApplicationEvent(ContextStartedEvent event) {
        println "@@@@Starting Up Our APP with profile : ${environment.getActiveProfiles().join(',')} "

        if (environment.getActiveProfiles().contains('dev')) {
            println("Let's inspect the beans provided by Spring Boot:")
            def beanNames = applicationContext.getBeanDefinitionNames().sort()
            beanNames.each { println it }
        }
    }
}
