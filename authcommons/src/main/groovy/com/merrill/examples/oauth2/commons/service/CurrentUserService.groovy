package com.merrill.examples.oauth2.commons.service

import com.merrill.examples.oauth2.commons.dao.user.AppUserDao
import com.merrill.examples.oauth2.commons.domain.user.AppUser
import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import com.merrill.examples.oauth2.commons.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by upaulm2 on 10/6/16.
 */
@Service
class CurrentUserService {


	@Autowired
	AppUserDao appUserDao

	@Autowired
	PasswordEncoder passwordEncoder

	/**
	 * Get the domain class instance associated with the current authentication.
	 * @return the user
	 */
	@Transactional(readOnly = true)
	def AppUser getCurrentUser() {
		if (!authentication?.authenticated) {
			return null
		}
		def principal = authentication.principal
		if (principal instanceof AppUserDetails) {
			def userDetails = (AppUserDetails) authentication.principal
			return appUserDao.findOne(userDetails.id)
		} else {
			def username = principal.toString()
			return appUserDao.findByUsername(username)
		}

	}


	def getAuthentication() {
		SecurityContextHolder.context.authentication
	}

	/**
	 * Encode the password using the configured PasswordEncoder.
	 */
	def String encodePassword (String password) {
		passwordEncoder.encode(password)
	}



}
