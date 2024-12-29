package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.ComplaintFormConstants;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import org.springframework.ui.Model;

import java.util.List;

/*
 * Contains methods for adding complaint-related data to the model.
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

    /*
     * Adds ComplaintIdentifierDTO to the model.
     * Adds a list of complaints to the model.
     * If the list is empty, a "noComplaintsFound" attribute with a message is added.
     * Otherwise, the list of complaints is added under the "complaints" attribute.
     */
    public static void addComplaintsToModel(List<ComplaintDTO> complaints, String noComplaintsMessage, Model model) {
        model.addAttribute("complaintIdentifierDTO", new ComplaintIdentifierDTO());
        model.addAttribute(
                complaints.isEmpty() ? "noComplaintsFound" : "complaints",
                complaints.isEmpty() ? noComplaintsMessage : complaints);
    }

    /*
     * Adds the complaint details for updating to the model and session.
     * Populates the dropdowns for product types and complaint statuses.
     */
    public static void addComplaintToModel(ComplaintDTO complaint, Model model) {
        model.addAttribute("complaint", complaint);
        populateDropDownsForProduct(complaint.getProductType(), model);
    }

    public static void populateComplaintUpdate(List<String> technicianIds, String updateEndpointOrigin, Model model) {
        model.addAttribute("technicianIds", technicianIds);
        populateComplaintStatus(model);
        model.addAttribute("updateEndpointOrigin", updateEndpointOrigin);
    }

    /*
     * Populates the model with available complaint statuses.
     */
    public static void populateComplaintStatus(Model model) {
        model.addAttribute("statuses", ComplaintStatus.values());
    }
}