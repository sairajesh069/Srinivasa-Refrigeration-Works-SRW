package com.srinivasa.refrigerationworks.srw.utility;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 * Utility class for fetching authenticated user's role.
 */
public class UserRoleProvider {

    /*
     * Returns the role of the authenticated user or "No User Found" if not authenticated.
     */
    public static String fetchUserRole(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            return authentication
                    .getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("No User Found");
        }
        return "No User Found";
    }
}