package com.srinivasa.refrigerationworks.srw.utility.common;

/*
 * Utility class for extracting substrings.
 */
public class SubStringExtractor {

    /*
     * Extracts a substring from the given text after the specified delimiter.
     *
     * @param text: The input text to process.
     * @param splitAt: The delimiter to split the text.
     * @return The substring after the delimiter, or an empty string if not found.
     */
    public static String extractSubString(String text, String splitAt) {
        String[] textSubStrings = text.split(splitAt);
        return textSubStrings.length > 1 ? textSubStrings[1] : "";
    }
}