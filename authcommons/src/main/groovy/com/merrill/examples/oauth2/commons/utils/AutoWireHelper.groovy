package com.merrill.examples.oauth2.commons.utils

/**
 * Created by upaulm2 on 1/12/17.
 */
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
final class AutoWireHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext
    private static final AutoWireHelper INSTANCE = new AutoWireHelper()

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        println "@@@@@@@@@ Setting application context ${applicationContext}"
        AutoWireHelper.applicationContext = applicationContext
    }

    /**
     * Tries to autowire the specified instance of the class if one of the specified beans which need to be autowired
     * are null.
     *
     * @param classToAutowire the instance of the class which holds @Autowire annotations
     * @param beansToAutowireInClass the beans which have the @Autowire annotation in the specified {#classToAutowire}
     */
    public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire)
                return
            }
        }
    }

    public static getBean(Class clazz) {
        applicationContext.getBean(clazz)
    }

    /**
     * @return the singleton instance.
     */
    public static AutoWireHelper getInstance() {
        INSTANCE;
    }
}
