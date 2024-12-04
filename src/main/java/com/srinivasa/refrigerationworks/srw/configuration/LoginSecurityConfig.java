package com.srinivasa.refrigerationworks.srw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Custom security configuration for login and access control
 */
@Configuration
public class LoginSecurityConfig {

    /*
     * In-memory user details configuration with username "sai" and role "USER"
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.builder()
                .username("sai")
                .password("{noop}1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /*
     * Security filter chain for request handling, login, and logout
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/", "/SRW/home").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/SRW/login")
                        .loginProcessingUrl("/authenticateUser")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
