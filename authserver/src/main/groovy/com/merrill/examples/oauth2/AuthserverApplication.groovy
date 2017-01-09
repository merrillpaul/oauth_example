package com.merrill.examples.oauth2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
public class AuthserverApplication extends WebMvcConfigurerAdapter {


	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args)
	}
}