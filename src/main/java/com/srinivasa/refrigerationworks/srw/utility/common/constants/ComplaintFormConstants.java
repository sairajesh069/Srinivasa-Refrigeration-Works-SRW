package com.srinivasa.refrigerationworks.srw.utility.common.constants;

import java.util.Arrays;
import java.util.List;

/*
 * Contains constants used for complaint form data
 */
public class ComplaintFormConstants {

    /*
     * List of product types available for selection in the complaint form.
     */
    public static final List<String> PRODUCT_TYPES = Arrays.asList("Air Conditioner", "Refrigerator", "Other");

    /*
     * List of air conditioner brands available for selection in the complaint form.
     */
    public static final List<String> AIR_CONDITIONER_BRANDS = Arrays.asList("Samsung", "LG", "Panasonic", "Daikin", "Haier", "Lloyd", "Hitachi", "BlueStar", "Carrier", "O'General", "Voltas", "Others");

    /*
     * List of air conditioner models available for selection in the complaint form.
     */
    public static final List<String> AIR_CONDITIONER_MODELS = Arrays.asList("Inverter Technology Split AC", "Non-Inverter Split AC", "Window Air Conditioner");

    /*
     * List of refrigerator brands available for selection in the complaint form.
     */
    public static final List<String> REFRIGERATOR_BRANDS = Arrays.asList("Samsung", "LG", "Whirlpool", "Haier", "Godrej", "Bosch", "Hitachi", "Kelvinator", "Voltas", "Panasonic", "Others");

    /*
     * List of refrigerator models available for selection in the complaint form.
     */
    public static final List<String> REFRIGERATOR_MODELS = Arrays.asList("Single-door Non-Inverter Technology", "Single-door Inverter Technology", "Double-door Non-Inverter Technology", "Double-door Inverter Technology");
}