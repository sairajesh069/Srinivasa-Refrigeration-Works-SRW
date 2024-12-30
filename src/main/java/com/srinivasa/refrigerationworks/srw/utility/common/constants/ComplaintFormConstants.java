package com.srinivasa.refrigerationworks.srw.utility.common.constants;

import java.util.Arrays;
import java.util.List;

/*
 * Constants for complaint form data.
 */
public class ComplaintFormConstants {

    /*
     * Available product types in the complaint form.
     */
    public static final List<String> PRODUCT_TYPES = Arrays.asList("Air Conditioner", "Refrigerator", "Other");

    /*
     * Available air conditioner brands.
     */
    public static final List<String> AIR_CONDITIONER_BRANDS = Arrays.asList("Samsung", "LG", "Panasonic", "Daikin", "Haier", "Lloyd", "Hitachi", "BlueStar", "Carrier", "O'General", "Voltas", "Others");

    /*
     * Available air conditioner models.
     */
    public static final List<String> AIR_CONDITIONER_MODELS = Arrays.asList("Inverter Technology Split AC", "Non-Inverter Split AC", "Window Air Conditioner");

    /*
     * Available refrigerator brands.
     */
    public static final List<String> REFRIGERATOR_BRANDS = Arrays.asList("Samsung", "LG", "Whirlpool", "Haier", "Godrej", "Bosch", "Hitachi", "Kelvinator", "Voltas", "Panasonic", "Others");

    /*
     * Available refrigerator models.
     */
    public static final List<String> REFRIGERATOR_MODELS = Arrays.asList("Single-door Non-Inverter", "Single-door Inverter", "Double-door Non-Inverter", "Double-door Inverter");
}