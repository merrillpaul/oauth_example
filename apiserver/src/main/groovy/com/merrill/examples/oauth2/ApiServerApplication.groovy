package com.merrill.examples.oauth2

import com.merrill.examples.oauth2.commons.jms.CustomerLocationTokenStorePublisher
import com.merrill.examples.oauth2.commons.provider.token.store.DefaultTokenStore
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import com.merrill.examples.oauth2.commons.service.impl.MongoOauthTokenStoreService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
public class ApiServerApplication extends WebMvcConfigurerAdapter {



	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args)
	}

	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		new BCryptPasswordEncoder()
	}


	/// all this to be removed when remoteTokenSrevice is added

	@Bean
	public TokenStore tokenStore() {
		new DefaultTokenStore()
	}

	@Bean
	public OauthTokenStoreService oauthTokenStoreService() {
		new MongoOauthTokenStoreService()
	}

	@Bean
	public CustomerLocationTokenStorePublisher customerLocationTokenStorePublisher() {
		new CustomerLocationTokenStorePublisher()
	}
}
