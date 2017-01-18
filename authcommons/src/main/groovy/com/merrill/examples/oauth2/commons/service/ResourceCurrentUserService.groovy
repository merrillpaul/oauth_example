package com.merrill.examples.oauth2.commons.service

import com.merrill.examples.oauth2.commons.dto.AppUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
 * Created by upaulm2 on 1/18/17.
 */
@Service
class ResourceCurrentUserService {

    def AppUserDetails getCurrentUser() {
        authentication.principal
    }

    def getAuthentication() {
        SecurityContextHolder.context.authentication
    }
}
