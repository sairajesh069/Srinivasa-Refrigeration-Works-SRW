package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.ComplaintFormConstants;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Model class for handling complaint-related attributes in the view.
 * Provides methods to add and populate complaint-related data to the model for rendering.
 */
public class ComplaintModel {

    /*
     * Adds an empty ComplaintDTO object to the model for complaint form binding.
     * Also adds available product types to the model.
     */
    public static void addComplaintDTOToModel(Model model) {
        model.addAttribute("complaintDTO", new ComplaintDTO());
        addProductTypesToModel(model);
    }

    /*
     * Adds available product types (Air Conditioner, Refrigerator, Other) to the model for dropdown selection.
     */
    public static void addProductTypesToModel(Model model) {
        model.addAttribute("productTypes", ComplaintFormConstants.PRODUCT_TYPES);
    }

    /*
     * Populates the dropdowns based on the selected product type.
     * Adds appropriate brands and product models to the model based on the product type.
     */
    public static void populateDropDownsForProduct(String productType, Model model) {
        addProductTypesToModel(model);

        switch(productType) {
            case "Refrigerator" -> {
                model.addAttribute("brands", ComplaintFormConstants.REFRIGERATOR_BRANDS);
                model.addAttribute("productModels", ComplaintFormConstants.REFRIGERATOR_MODELS);
            }
            case "Air Conditioner" -> {
                model.addAttribute("brands", ComplaintFormConstants.AIR_CONDITIONER_BRANDS);
                model.addAttribute("productModels", ComplaintFormConstants.AIR_CONDITIONER_MODELS);
            }
            case "Other" -> {
                model.addAttribute("brands", List.of("Other"));
                model.addAttribute("productModels", List.of("Other"));
            }
            default -> {
                model.addAttribute("brands", List.of());
                model.addAttribute("productModels", List.of());
            }
        }
    }
}