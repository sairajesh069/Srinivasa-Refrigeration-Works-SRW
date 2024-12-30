package com.srinivasa.refrigerationworks.srw.configuration;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 * Custom implementation of RoleHierarchy.
 * - Defines a hierarchy where higher roles inherit permissions of lower roles.
 */
public class CustomRoleHierarchy implements RoleHierarchy {

    /*
     * Overrides getReachableGrantedAuthorities to return reachable authorities based on role hierarchy.
     */
    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {

        /*
         * Set of authorities that are reachable based on the current roles.
         */
        Set<GrantedAuthority> reachableAuthorities = new HashSet<>(authorities);

        /*
         * Determine additional reachable roles based on the current authority.
         */
        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case "ROLE_OWNER" -> {
                    /*
                     * If the user has the ROLE_OWNER authority,
                     * they also have the rights of ROLE_EMPLOYEE and ROLE_CUSTOMER.
                     */
                    reachableAuthorities.add(() -> "ROLE_EMPLOYEE");
                    reachableAuthorities.add(() -> "ROLE_CUSTOMER");
                }
                case "ROLE_EMPLOYEE" ->
                    /*
                     * If the user has the ROLE_EMPLOYEE authority,
                     * they also have the rights of ROLE_CUSTOMER.
                     */
                        reachableAuthorities.add(() -> "ROLE_CUSTOMER");
            }
        }

        return reachableAuthorities;
    }
}