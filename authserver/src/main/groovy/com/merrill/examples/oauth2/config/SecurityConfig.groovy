package com.merrill.examples.oauth2.config

import com.merrill.examples.oauth2.commons.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by upaulm2 on 1/3/17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService

    @Autowired
    PasswordEncoder passwordEncoder


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
       /*auth.inMemoryAuthentication()
                .withUser("john_us").password("123").roles("USER")
                .and()
                .withUser("john_fr").password("123").roles("USER")*/
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder)

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        super.authenticationManagerBean()
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable()


    }
}
