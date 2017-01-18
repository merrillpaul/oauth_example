package com.merrill.examples.oauth2

import com.merrill.examples.oauth2.commons.jms.CustomerLocationTokenStorePublisher
import com.merrill.examples.oauth2.commons.provider.token.converter.AuthServerAccessTokenConverter
import com.merrill.examples.oauth2.commons.provider.token.store.DefaultTokenStore
import com.merrill.examples.oauth2.commons.service.OauthTokenStoreService
import com.merrill.examples.oauth2.commons.service.impl.MongoOauthTokenStoreService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

import javax.jms.ConnectionFactory

@SpringBootApplication
@EnableJms
public class AuthserverApplication extends WebMvcConfigurerAdapter {



	public static void main(String[] args) {
		def ctx = SpringApplication.run(AuthserverApplication.class, args)
		ctx.start()
	}

	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		new BCryptPasswordEncoder()
	}

	@Bean
	public JmsListenerContainerFactory<?> tokenJmsFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory()
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory)
		// You could still override some of Boot's default if necessary.
		factory
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter()
		converter.setTargetType(MessageType.TEXT)
		converter.setTypeIdPropertyName("_type")
		converter
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
	public TokenStore tokenStore() {
		new DefaultTokenStore()
	}

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		new AuthServerAccessTokenConverter()
	}



		@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter()  {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// TODO the allowable origins should be based on the environment from application.yml
				registry.addMapping("/*").allowedOrigins("*")
			}
		}
	}
}
