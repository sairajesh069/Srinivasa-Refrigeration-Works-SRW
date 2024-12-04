package com.srinivasa.refrigerationworks.srw.utility.common;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;

/*
 * Utility class for trimming whitespace from strings
 */
public class StringEditor {

    /*
     * Registers a custom editor to trim leading and trailing spaces from String fields
     */
    public static void stringTrimmer(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringEditor);
    }
}