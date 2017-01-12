package com.merrill.examples.oauth2.commons.domain.user

import com.merrill.examples.oauth2.commons.domain.AbstractEntity

import javax.persistence.Column
import javax.persistence.Entity

/**
 * Created by upaulm2 on 1/12/17.
 */
@Entity
class BusinessEntity extends AbstractEntity {

    @Column
    String name

    //just an example
    @Column
    String tokenStoreUrl
}
