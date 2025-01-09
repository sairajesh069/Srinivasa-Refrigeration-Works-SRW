package com.srinivasa.refrigerationworks.srw.model;

import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.EmployeeInfoDTO;
import com.srinivasa.refrigerationworks.srw.utility.common.constants.ComplaintFormConstants;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.ComplaintStatus;
import com.srinivasa.refrigerationworks.srw.utility.common.enums.UserStatus;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/*
 * Contains methods for adding complaint-related data to the model.
 */
public class ComplaintModel {

    /*
     * Adds an empty ComplaintDTO and product types to the model for form binding.
     */
    public static void addComplaintDTOToModel(Model model) {
        model.addAttribute("complaintDTO", new ComplaintDTO());
        addProductTypesToModel(model);
    }

    /*
     * Adds available product types to the model for dropdown selection.
     */
    public static void addProductTypesToModel(Model model) {
        model.addAttribute("productTypes", ComplaintFormConstants.PRODUCT_TYPES);
    }

    /*
     * Populates dropdowns based on selected product type.
     * Adds brands and models to the model based on the product type.
     */
    public static void populateDropDownsForProduct(String productType, Model model) {
        addProductTypesToModel(model);
        if (productType != null) {
            switch (productType) {
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
                    model.addAttribute("brands", List.of(""));
                    model.addAttribute("productModels", List.of(""));
                }
            }
        }
    }

    /*
     * Adds ComplaintIdentifierDTO, techniciansInfo and a list of complaints to the model.
     * If empty, adds a "noComplaintsFound" attribute with a message.
     */
    public static void addComplaintsToModel(Map<String, EmployeeInfoDTO> techniciansInfo, ComplaintIdentifierDTO complaintIdentifierDTO, List<ComplaintDTO> complaints, String noComplaintsMessage, Model model) {
        model.addAttribute("complaintIdentifierDTO", complaintIdentifierDTO);
        model.addAttribute("techniciansInfo", techniciansInfo);
        model.addAttribute(
                complaints.isEmpty() ? "noComplaintsFound" : "complaints",
                complaints.isEmpty() ? noComplaintsMessage : complaints);
    }

    /*
     * Adds complaint details for updating to the model and session.
     * Populates dropdowns for product types and complaint statuses.
     */
    public static void addComplaintToModel(ComplaintDTO complaint, Model model) {
        model.addAttribute("complaint", complaint);
        populateDropDownsForProduct(complaint.getProductType(), model);
    }

    /*
     * Populates the model with technicians, complaint statuses, and update endpoint.
     */
    public static void populateComplaintUpdate(Map<String, EmployeeInfoDTO> techniciansInfo, String updateEndpointOrigin, Model model) {
        model.addAttribute("activeTechniciansInfo", techniciansInfo
                            .values()
                            .stream()
                            .filter(employeeInfoDTO -> employeeInfoDTO.getStatus().equals(UserStatus.ACTIVE))
                            .map(employee -> employee.getEmployeeId() + " - " + employee.getFullName())
                            .toList());
        model.addAttribute("techniciansInfo", techniciansInfo);
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