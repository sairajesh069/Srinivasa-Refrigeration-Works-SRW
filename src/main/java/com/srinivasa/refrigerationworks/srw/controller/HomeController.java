package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.UserCredentialModel;
import com.srinivasa.refrigerationworks.srw.service.UserCredentialService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/*
 * Controller class for handling home page requests.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    /*
     * Service for managing user credentials.
     */
    private final UserCredentialService userCredentialService;

    /*
     * Handles GET requests for the root ("/") endpoint.
     * Redirects to the "/SRW/home" endpoint.
     */
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/SRW/home";
    }

    /*
     * Handles GET requests for the home page.
     * - Adds the user profile link to the model based on the user's role.
     * - Sets the user ID in the session if the principal is not null.
     */
    @GetMapping("/SRW/home")
    public String home(Model model, HttpSession session, Principal principal) {
        UserCredentialModel.addUserProfileHrefToModel(UserRoleProvider.fetchUserRole(session), model);
        if (principal != null) {
            session.setAttribute("userId", userCredentialService.getUserIdByUsername(principal.getName()));
        }
        return "home";
    }

    /*
     * Handles GET requests for the SRW/management-portal endpoint.
     * Returns the "management-portal" view.
     */
    @GetMapping("SRW/management-portal")
    public String managementPortal() {
        return "management-portal";
    }
}