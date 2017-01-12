package com.merrill.examples.oauth2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
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
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter()  {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// TODO the allowable origins should be based on the environment from application.yml
				registry.addMapping("/api/*").allowedOrigins("*")
			}
		};
	}
}
