package com.srinivasa.refrigerationworks.srw.utility;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 * Utility class to fetch the role of the authenticated user.
 * - Retrieves the role from the SecurityContext if the user is authenticated.
 * - Returns "No User Found" if no user is authenticated.
 */
public class UserRoleProvider {

    /*
     * Fetches the authenticated user's role from the SecurityContext.
     * @param session The current HTTP session.
     * @return The role of the authenticated user or "No User Found" if not authenticated.
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