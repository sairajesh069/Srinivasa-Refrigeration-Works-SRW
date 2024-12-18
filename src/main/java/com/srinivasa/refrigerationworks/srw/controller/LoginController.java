package com.srinivasa.refrigerationworks.srw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller class for handling login page requests
 */
@Controller
@RequestMapping("/SRW")
public class LoginController {

    /*
     * Method to handle GET requests for the /SRW/login endpoint
     * Returns the custom login page view
     */
    @GetMapping("/login")
    public String loginPage() {
        return "custom-login";
    }

    /*
     * Method to handle GET requests for the /SRW/access-denied endpoint
     * Returns the access denied page view
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}