package com.merrill.examples.oauth2.commons.jpa.listeners

import com.merrill.examples.oauth2.commons.domain.user.AppUser
import com.merrill.examples.oauth2.commons.utils.AutoWireHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import javax.persistence.PrePersist

/**
 * Created by upaulm2 on 9/13/16.
 */
@Component
class AppUserEntityListener {


	@PrePersist
	public void onBeforeInsert(AppUser userEntity) {
		PasswordEncoder passwordEncoder = AutoWireHelper.getBean(PasswordEncoder.class)
		//println "Before insert password encoder ${passwordEncoder}"
		if(userEntity.password) {
			userEntity.password = passwordEncoder.encode(userEntity.password)
		}
	}
}
