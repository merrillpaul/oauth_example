package com.merrill.examples.oauth2.commons.domain

import com.merrill.examples.oauth2.commons.jpa.listeners.AbstractEntityListener
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient
import javax.persistence.Version
import java.sql.Timestamp

/**
 * Created by upaulm2 on 1/12/17.
 */
@EntityListeners(AbstractEntityListener)
@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    String id

    /** The created by. */
    @Column
    String createdBy

    /** The last created. */
    @Column
    Timestamp lastCreated

    /** The last updated. */
    @Column
    Timestamp lastUpdated

    /** The updated by. */
    @Column
    String updatedBy

    /** The created by full name. */
    @Transient
    String createdByFullName

    /** The updated by full name. */
    @Transient
    String updatedByFullName

    @Version
    Long version

}
