package com.srinivasa.refrigerationworks.srw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
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
     * Bean configuration to provide a custom RoleHierarchy.
     * - This defines a custom hierarchy of roles with inheritance,
     *   allowing roles to implicitly have the authorities of other roles.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return new CustomRoleHierarchy();
    }

    /*
     * Security filter chain configuration for HTTP request authorization.
     * - Defines access rules for different URLs based on user roles.
     * - Allows access to specific paths without authentication (e.g., CSS, home, customer registration).
     * - Restricts access to certain paths for specific roles (e.g., OWNER, CUSTOMER).
     * - Configures custom login and logout settings.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(configurer -> configurer
                        /*
                         * Allow access to static CSS files and the home page without authentication.
                         */
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/", "/SRW/home").permitAll()

                        /*
                         * Allow customer registration and confirmation pages to be accessed by anyone.
                         */
                        .requestMatchers("/SRW/customer/register", "/SRW/customer/confirmation").permitAll()

                        /*
                         * Restrict access to owner-related pages to users with the "OWNER" role.
                         */
                        .requestMatchers("/SRW/owner/**", "/SRW/management-portal").hasRole("OWNER")

                        /*
                         * Restrict access to employee registration and confirmation to users with the "OWNER" role.
                         */
                        .requestMatchers("/SRW/employee/register", "/SRW/employee/confirmation").hasRole("OWNER")

                        /*
                         * Require authentication for any other request.
                         */
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        /*
                         * Configure custom login page and processing URL.
                         */
                        .loginPage("/SRW/login")
                        .loginProcessingUrl("/authenticateUser")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
