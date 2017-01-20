package com.merrill.examples.oauth2

import com.merrill.examples.oauth2.commons.filters.SimpleCorsFilter
import com.merrill.examples.oauth2.commons.jms.CustomerLocationTokenStorePublisher
import com.merrill.examples.oauth2.commons.provider.token.converter.ResourceServerAccessTokenConverter
import com.merrill.examples.oauth2.commons.provider.token.store.DefaultTokenStore
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import com.merrill.examples.oauth2.commons.service.impl.MongoOauthTokenStoreService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
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

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		new ResourceServerAccessTokenConverter()
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// TODO the allowable origins should be based on the environment from application.yml
				registry.addMapping("/*").allowedOrigins("*").allowedMethods(
						RequestMethod.POST.name(), RequestMethod.GET.name(), RequestMethod.OPTIONS.name(),
						RequestMethod.PUT.name(), RequestMethod.DELETE.name()
				).maxAge(4800)
			}
		}
	}


	// TO allow preflight PUTS
	@Bean
	public FilterRegistrationBean corsFilterChain() {
		FilterRegistrationBean registration = new FilterRegistrationBean(new SimpleCorsFilter())
		registration.setOrder(Integer.MIN_VALUE)
		registration.setName("simpleCorsFilter")
		registration
	}
}
