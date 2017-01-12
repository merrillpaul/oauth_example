package com.merrill.examples.oauth2.commons.dao.user

import com.merrill.examples.oauth2.commons.domain.user.BusinessEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by upaulm2 on 1/12/17.
 */
interface BusinessEntityDao extends JpaRepository<BusinessEntity, String> {

}