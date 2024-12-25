package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.ComplaintFormConstants;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import jakarta.servlet.http.HttpSession;
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
     * Adds a list of complaints to the model.
     * If the list is empty, a "noRecordsFound" attribute with a message is added.
     * Otherwise, the list of complaints is added under the "complaints" attribute.
     */
    public static void addComplaintListToModel(List<ComplaintDTO> complaints, Model model) {
        model.addAttribute(
                complaints.isEmpty() ? "noComplaintsFound" : "complaints",
                complaints.isEmpty() ? "You have not registered any complaints." : complaints);
    }

    /*
     * Adds an empty ComplaintIdentifierDTO object to the model, used for capturing the complaint identifier input.
     */
    public static void addComplaintIdentifierDTOToModel(Model model) {
        model.addAttribute("complaintIdentifierDTO", new ComplaintIdentifierDTO());
    }

    /*
     * Adds complaint details to the model based on the size of the complaint list.
     * - If no complaints, adds a "noComplaintFound" attribute with a message.
     * - If exactly one complaint, adds the "complaint" attribute with the single complaint.
     * - If multiple complaints, adds the "complaints" attribute with the list of complaints.
     */
    public static void addComplaintDetailsToModel(List<ComplaintDTO> complaints, Model model) {
        switch(complaints.size()) {
            case 0 -> model.addAttribute("noComplaintFound", "Complaint not found for the given details.");

            case 1 -> model.addAttribute("complaint", complaints.get(0));

            default -> model.addAttribute("complaints", complaints);
        }
    }

    /*
     * Adds the complaint details for updating to the model and session.
     * Populates the dropdowns for product types and complaint statuses.
     */
    public static void addComplaintDTOForUpdateToModel(ComplaintDTO complaintDTO, List<String> technicianIds,
                                                       Model model, HttpSession session) {
        if(complaintDTO != null) {
            model.addAttribute("complaintDTO", complaintDTO);
            session.setAttribute("initialComplaintDTO", complaintDTO);
            populateDropDownsForProduct(complaintDTO.getProductType(), model);
            populateComplaintStatus(model);
        }
        session.setAttribute("canAccess", complaintDTO != null);
        populateTechnicianIds(technicianIds, model);
        session.setAttribute("technicianIds", technicianIds);
    }

    /*
     * Populates technician IDs into the model for use in views.
     */
    public static void populateTechnicianIds(List<String> technicianIds, Model model) {
        model.addAttribute("technicianIds", technicianIds);
    }

    /*
     * Populates the model with available complaint statuses.
     */
    public static void populateComplaintStatus(Model model) {
        model.addAttribute("statuses", ComplaintStatus.values());
    }
}