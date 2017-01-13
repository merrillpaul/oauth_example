package com.merrill.examples.oauth2.commons.jms

import com.merrill.examples.oauth2.commons.jms.dto.TokenMessagePayload
import com.merrill.examples.oauth2.commons.jms.dto.TokenMessageType
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.jms.annotation.JmsListener

/**
 * Created by upaulm2 on 1/13/17.
 */
abstract class AbstractTokenMessageReceiver implements TokenMessageReceiver, BeanFactoryAware {


    BeanFactory beanFactory

    @Override
    void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory
    }

    @JmsListener(destination = TokenMessagePayload.DESTINATION, containerFactory = "tokenJmsFactory")
    def receiveMessage(TokenMessagePayload tokenMessagePayload) {
        processPayload(tokenMessagePayload)
    }

    def final getOrAddBeans(String key, Closure beanProvider = null) {
        def hasBean = beanFactory.containsBean(key)
        def bean
        if (hasBean) {
            bean = beanFactory.getBean(key)
        } else if (!hasBean && beanProvider) {
            bean = beanProvider()
            ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory
            configurableBeanFactory.registerSingleton(key, bean)
        }
        bean
    }

    abstract processPayload(TokenMessagePayload tokenMessagePayload)
}
