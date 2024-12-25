package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.ComplaintModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.ComplaintService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
import com.srinivasa.refrigerationworks.srw.validation.ComplaintIdentifierValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Controller class to handle complaint-related operations.
 */
@Controller
@RequestMapping("/SRW/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    /*
     * Initializes data binder to trim input strings.
     */
    @InitBinder
    public void initialize(WebDataBinder webDataBinder) {
        StringEditor.stringTrimmer(webDataBinder);
    }

    /*
     * Displays the complaint registration form.
     */
    @GetMapping("/register")
    public String bookRepair(Model model) {
        ComplaintModel.addComplaintDTOToModel(model);
        return "complaint/complaint-register-form";
    }

    /*
     * Handles form submission for registering a complaint.
     * Validates the complaint data, processes the complaint registration,
     * and redirects to the confirmation page if successful.
     * If validation fails, it re-displays the registration form with error messages.
     */
    @PostMapping("/confirmation")
    public String registerComplaint(@ModelAttribute @Valid ComplaintDTO complaintDTO, BindingResult bindingResult,
                                    Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            if(complaintDTO.getProductType() != null) {
                ComplaintModel.populateDropDownsForProduct(complaintDTO.getProductType(), model);
            }
            else {
                ComplaintModel.addProductTypesToModel(model);
            }
            return "complaint/complaint-register-form";
        }
        complaintService.registerComplaint(complaintDTO, (String) session.getAttribute("userId"));
        return "complaint/complaint-confirmation";
    }

    /*
     * Updates dropdown options based on selected product type.
     */
    @PostMapping("/update-dropdown")
    public String bookRepair(ComplaintDTO complaintDTO, Model model) {
        ComplaintModel.populateDropDownsForProduct(complaintDTO.getProductType(), model);
        if(complaintDTO.getComplaintId() != null) {
            ComplaintModel.populateComplaintStatus(model);
            return "complaint/complaint-update-form";
        }
        return "complaint/complaint-register-form";
    }

    /*
     * Handles the GET request to display a list of complaints registered by the logged-in user.
     */
    @GetMapping("/my-complaints")
    public String getMyComplaints(Model model, HttpSession session) {
        ComplaintModel.addComplaintListToModel(
                complaintService.getComplaintsByBookedById((String) session.getAttribute("userId")), model);
        return "complaint/complaint-list";
    }

    /*
     * Handles the GET request to display the list of all complaints.
     * Retrieves all complaints and adds them to the model for rendering.
     */
    @GetMapping("/list")
    public String getComplaintList(Model model) {
        ComplaintModel.addComplaintListToModel(complaintService.getComplaintList(), model);
        return "complaint/complaint-list";
    }

    /*
     * Handles the GET request to display the list of all active complaints.
     * Retrieves all active complaints and adds them to the model for rendering.
     */
    @GetMapping("/active-list")
    public String getActiveComplaintList(Model model) {
        ComplaintModel.addComplaintListToModel(complaintService.getActiveComplaintList(), model);
        return "complaint/complaint-list";
    }

    /*
     * Handles GET requests to display the complaint search form.
     * Adds a ComplaintIdentifierDTO object to the model for capturing user input.
     */
    @GetMapping("/search")
    public String getComplaint(Model model) {
        ComplaintModel.addComplaintIdentifierDTOToModel(model);
        return "complaint/complaint-details";
    }

    /*
     * Handles POST requests to search for complaints based on the identifier.
     * Validates the identifier and fetches complaint details or a list of complaints.
     * Displays complaint details if one result is found, or a list view for multiple results.
     */
    @PostMapping("/search")
    public String getComplaint(ComplaintIdentifierDTO complaintIdentifierDTO, BindingResult bindingResult,
                               HttpSession session, Model model) {

        ComplaintIdentifierValidation.identifierValidation(complaintIdentifierDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return "complaint/complaint-details";
        }
        List<ComplaintDTO> complaints = complaintService.getComplaintByIdentifier(
                complaintIdentifierDTO, (String) session.getAttribute("userId"), UserRoleProvider.fetchUserRole(session));
        ComplaintModel.addComplaintDetailsToModel(complaints, model);
        return (complaints.size() <= 1) ? "complaint/complaint-details" : "complaint/complaint-list";
    }

    /*
     * Handles the GET request to display the complaint update form with the complaint's existing details.
     * Sets session attribute using substring from the "Referer" header.
     */
    @GetMapping("/update")
    public String updateComplaint(@RequestParam("complaintId") String complaintId, Model model,
                                  HttpSession session, HttpServletRequest request) {

        ComplaintModel.addComplaintDTOForUpdateToModel(
                complaintService.getComplaintById(complaintId, UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER"),
                        (String) session.getAttribute("userId")), model, session);

        session.setAttribute("updateEndpointOrigin", SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/"));
        return (boolean)session.getAttribute("canAccess") ? "complaint/complaint-update-form" : "access-denied";
    }

    /*
     * Handles the POST request to update a complaint after validating the input.
     * Redirects to the appropriate origin complaint page of update endpoint on success based on the user's role.
     */
    @PostMapping("/update")
    public String updateComplaint(@ModelAttribute("complaintDTO") @Valid ComplaintDTO updatedComplaintDTO,
                                  BindingResult bindingResult, Model model, HttpSession session) {

        if(bindingResult.hasErrors()) {
            ComplaintModel.populateDropDownsForProduct(updatedComplaintDTO.getProductType(), model);
            ComplaintModel.populateComplaintStatus(model);
            return "complaint/complaint-update-form";
        }
        ComplaintDTO initialComplaintDTO = (ComplaintDTO) session.getAttribute("initialComplaintDTO");
        complaintService.updateComplaint(initialComplaintDTO, updatedComplaintDTO);
        return "redirect:/SRW/complaint/" + session.getAttribute("updateEndpointOrigin");
    }

    /*
     * Handles the GET request to activate a complaint.
     * - Activates the complaint based on the provided complaintId.
     * - Redirects to the originating complaint list page upon success.
     */
    @GetMapping("/activate")
    public String activateComplaint(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        complaintService.activateComplaint(complaintId);
        return "redirect:/SRW/complaint/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/");
    }

    /*
     * Handles the GET request to deactivate a complaint.
     * - Deactivates the complaint based on the provided complaintId.
     * - Redirects to the originating complaint list page upon success.
     */
    @GetMapping("/deactivate")
    public String deactivateComplaint(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        complaintService.deactivateComplaint(complaintId);
        return "redirect:/SRW/complaint/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/");
    }

    /*
     * Retrieves complaints assigned to the logged-in technician and adds them to the model.
     * Returns the view for displaying the list of complaints.
     */
    @GetMapping("/assigned-complaints")
    public String getAssignedComplaints(Model model, HttpSession session) {
        ComplaintModel.addComplaintListToModel(
                complaintService.getComplaintsByTechnicianId((String) session.getAttribute("userId")), model);
        return "complaint/complaint-list";
    }
}