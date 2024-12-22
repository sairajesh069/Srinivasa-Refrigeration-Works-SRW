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
                         * Allow username recovery and password reset to be accessed by anyone.
                         */
                        .requestMatchers("/SRW/username-recovery", "/SRW/password-reset").permitAll()

                        /*
                         * Restricts access to specific resources and actions to users with the "OWNER" role.
                         * - "/SRW/owner/**", "/SRW/management-portal": Full access to owner-related endpoints and the management portal.
                         * - "/SRW/employee/register", "/SRW/employee/confirmation": Access to employee registration and confirmation pages.
                         * - "/SRW/employee/list", "/SRW/customer/list", "/SRW/complaint/list": Access to various list pages.
                         * - "/SRW/employee/active-list", "/SRW/customer/active-list", "/SRW/complaint/active-list": Access to various active-list pages.
                         * - "/SRW/employee/search", "/SRW/customer/search": Access to various search pages.
                         * - "/SRW/employee/update", "/SRW/customer/update": Access to various update pages.
                         * - "/SRW/employee/activate", "/SRW/customer/activate", "/SRW/complaint/activate": Access to various activate pages.
                         * - "/SRW/employee/deactivate", "/SRW/customer/deactivate", "/SRW/complaint/deactivate": Access to various deactivate pages.
                         */
                        .requestMatchers("/SRW/owner/**", "/SRW/management-portal").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/register", "/SRW/employee/confirmation").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/list", "/SRW/customer/list", "/SRW/complaint/list").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/active-list", "/SRW/customer/active-list", "/SRW/complaint/active-list").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/search", "/SRW/customer/search").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/update", "/SRW/customer/update").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/activate", "/SRW/customer/activate", "/SRW/complaint/activate").hasRole("OWNER")
                        .requestMatchers("/SRW/employee/deactivate", "/SRW/customer/deactivate", "/SRW/complaint/deactivate").hasRole("OWNER")

                        /*
                         * Restrict access to complaint registration, confirmation, viewing, fetching and updating own complaints to "CUSTOMER" role.
                         */
                        .requestMatchers("/SRW/complaint/register", "/SRW/complaint/confirmation").hasRole("CUSTOMER")
                        .requestMatchers("/SRW/complaint/my-complaints", "/SRW/complaint/search").hasRole("CUSTOMER")
                        .requestMatchers("/SRW/complaint/update").hasRole("CUSTOMER")

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
                .exceptionHandling(customizer -> customizer
                        /*
                         * Configures custom exception handling for access control.
                         * - Redirects users to a custom "Access Denied" page when they attempt to access restricted resources.
                         */
                        .accessDeniedPage("/SRW/access-denied"))
                .build();
    }
}