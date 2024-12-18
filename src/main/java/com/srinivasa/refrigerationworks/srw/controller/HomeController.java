package com.srinivasa.refrigerationworks.srw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller class for handling home page requests
 */
@Controller
public class HomeController {

    /*
     * Method to handle GET requests for the root ("/") endpoint
     * Redirects to "/SRW/home"
     */
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/SRW/home";
    }

    /*
     * Method to handle GET requests for the SRW/home endpoint
     * Returns the "home" view
     */
    @GetMapping("/SRW/home")
    public String home() {
        return "home";
    }

    /*
     * Method to handle GET requests for the SRW/management-portal endpoint
     * Returns the "management-portal" view
     */
    @GetMapping("SRW/management-portal")
    public String managementPortal() {
        return "management-portal";
    }
}