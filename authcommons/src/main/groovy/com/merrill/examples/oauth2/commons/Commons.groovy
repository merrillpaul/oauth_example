package com.merrill.examples.oauth2.commons

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

//@SpringBootApplication
public class Commons extends WebMvcConfigurerAdapter {


	public static void main(String[] args) {
		SpringApplication.run(Commons.class, args)
	}
}
