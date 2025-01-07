package com.srinivasa.refrigerationworks.srw.utility.common;

import jakarta.servlet.http.HttpServletRequest;

/*
 * Utility class to extract specific endpoint information from the Referer header of HTTP requests.
 */
public class EndpointExtractor {

    /*
     * Extracts a substring from the Referer header of the HTTP request, starting with the given prefix.
     */
    public static String endpoint(HttpServletRequest request, String prefix) {
        return SubStringExtractor.extractSubString(request.getHeader("Referer"), prefix);
    }

    /*
     * Extracts the owner-related endpoint from the Referer header.
     */
    public static String ownerEndpoint(HttpServletRequest request) {
        return endpoint(request, "owner/");
    }

    /*
     * Extracts the employee-related endpoint from the Referer header.
     */
    public static String employeeEndpoint(HttpServletRequest request) {
        return endpoint(request, "employee/");
    }

    /*
     * Extracts the customer-related endpoint from the Referer header.
     */
    public static String customerEndpoint(HttpServletRequest request) {
        return endpoint(request, "customer/");
    }

    /*
     * Extracts the complaint-related endpoint from the Referer header.
     */
    public static String complaintEndpoint(HttpServletRequest request) {
        return endpoint(request, "complaint/");
    }
}
