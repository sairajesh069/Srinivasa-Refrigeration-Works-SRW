package com.srinivasa.refrigerationworks.srw.utility.common;

/*
 * Utility class for formatting phone numbers.
 * Adds country code (+91) if not already present.
 */
public class PhoneNumberFormatter {

    public static String formatPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+91") ? phoneNumber : "+91" + phoneNumber;
    }
}