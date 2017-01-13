package com.merrill.examples.oauth2.commons.utils

import org.springframework.beans.MutablePropertyValues
import org.springframework.boot.bind.RelaxedDataBinder

/**
 * Created by upaulm2 on 1/13/17.
 */
class DataBinder {
    static void populateObjectFromProperties(def target, def properties) {
        new RelaxedDataBinder(target).bind(new MutablePropertyValues(properties))
    }
}
