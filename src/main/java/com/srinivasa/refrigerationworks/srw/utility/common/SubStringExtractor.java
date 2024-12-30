package com.srinivasa.refrigerationworks.srw.utility.common;

/*
 * Utility class for extracting substrings
 */
public class SubStringExtractor {

    /*
     * Extracts a substring from text after the specified delimiter
     */
    public static String extractSubString(String text, String splitAt) {
        String[] textSubStrings = text.split(splitAt);
        return textSubStrings.length > 1 ? textSubStrings[1] : "";
    }
}