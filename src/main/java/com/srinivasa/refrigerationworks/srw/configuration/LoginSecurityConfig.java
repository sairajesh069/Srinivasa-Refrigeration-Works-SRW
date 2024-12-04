package com.srinivasa.refrigerationworks.srw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/*
 * Custom security configuration for login and access control
 */
@Configuration
public class LoginSecurityConfig {

    /*
     * Configures and returns a UserDetailsManager for managing user authentication and authorities.
     * Uses JDBC to fetch user credentials and roles from the database.
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        /*
         * SQL query to fetch user credentials (username, password, enabled status) from the database.
         */
        String usersByUserNameQuery = """
                SELECT username, password, enabled
                FROM user_credentials
                WHERE username=?;
                """;

        /*
         * SQL query to fetch user roles (roles associated with username) from the database.
         */
        String authoritiesByUserNameQuery = """
                SELECT username, roles
                FROM user_roles
                WHERE username=?;
                """;

        userDetailsManager.setUsersByUsernameQuery(usersByUserNameQuery);
        userDetailsManager.setAuthoritiesByUsernameQuery(authoritiesByUserNameQuery);

        return userDetailsManager;
    }

    /*
     * Bean configuration for PasswordEncoder using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
