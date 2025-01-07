package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.ComplaintModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.ComplaintService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import com.srinivasa.refrigerationworks.srw.utility.common.EndpointExtractor;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/*
 * Controller class to handle complaint-related operations.
 */
@Controller
@RequestMapping("/SRW/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    /*
     * Service for handling complaint-related data and operations.
     */
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
     */
    @PostMapping("/confirmation")
    public String registerComplaint(@ModelAttribute @Valid ComplaintDTO complaintDTO, BindingResult bindingResult,
                                    Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            if (complaintDTO.getProductType() != null) {
                ComplaintModel.populateDropDownsForProduct(complaintDTO.getProductType(), model);
            } else {
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
    public String bookRepair(ComplaintDTO complaint, Model model, HttpServletRequest request) {
        if (complaint.getComplaintId() != null) {
            ComplaintModel.addComplaintToModel(complaint, model);
            ComplaintModel.populateComplaintUpdate(
                    complaintService.getActiveTechnicians(), request.getParameter("updateEndpointOrigin"), model);
            ComplaintModel.populateComplaintStatus(model);
            return "complaint/complaint-update-form";
        }
        ComplaintModel.populateDropDownsForProduct(complaint.getProductType(), model);
        return "complaint/complaint-register-form";
    }

    /*
     * Displays a list of complaints registered by the logged-in user.
     */
    @GetMapping("/my-complaints")
    public String getMyComplaints(Model model, HttpSession session) {
        ComplaintIdentifierDTO complaintIdentifierDTO = (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO");
        ComplaintModel.addComplaintsToModel(complaintService.getActiveTechniciansInfo(),
                complaintIdentifierDTO == null ? new ComplaintIdentifierDTO() : complaintIdentifierDTO,
                complaintService.getComplaintsByBookedById((String) session.getAttribute("userId")),
                "No complaints have been registered yet.", model);
        return "complaint/complaint-list";
    }

    /*
     * Displays the list of all complaints.
     */
    @GetMapping("/list")
    public String getComplaintList(Model model) {
        ComplaintIdentifierDTO complaintIdentifierDTO = (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO");
        ComplaintModel.addComplaintsToModel(complaintService.getActiveTechniciansInfo(),
                complaintIdentifierDTO == null ? new ComplaintIdentifierDTO() : complaintIdentifierDTO,
                complaintService.getComplaintList(), "No complaints have been registered yet.", model);
        return "complaint/complaint-list";
    }

    /*
     * Displays the list of all active complaints.
     */
    @GetMapping("/active-list")
    public String getActiveComplaintList(Model model) {
        ComplaintIdentifierDTO complaintIdentifierDTO = (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO");
        ComplaintModel.addComplaintsToModel(complaintService.getActiveTechniciansInfo(),
                complaintIdentifierDTO == null ? new ComplaintIdentifierDTO() : complaintIdentifierDTO,
                complaintService.getActiveComplaintList(), "No active complaints found.", model);
        return "complaint/complaint-list";
    }

    /*
     * Searches for complaints based on the identifier and displays results.
     */
    @PostMapping("/search")
    public String getComplaint(@ModelAttribute @Valid ComplaintIdentifierDTO complaintIdentifierDTO, BindingResult bindingResult,
                               HttpSession session, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String refererEndpoint = EndpointExtractor.complaintEndpoint(request);
        String searchEndpointOrigin = refererEndpoint.equals("search")
                ? (String) session.getAttribute("searchEndpointOrigin") : refererEndpoint;
        if(!bindingResult.hasErrors() && complaintIdentifierDTO.getIdentifier() != null) {
            ComplaintModel.addComplaintsToModel(complaintService.getActiveTechniciansInfo(), complaintIdentifierDTO,
                    complaintService.getComplaintByIdentifier(
                            complaintIdentifierDTO, (String) session.getAttribute("userId"), UserRoleProvider.fetchUserRole(session)
                    ), "No complaints found.", model);
            session.setAttribute("searchEndpointOrigin", searchEndpointOrigin);
            return "complaint/complaint-list";
        }
        redirectAttributes.addFlashAttribute("complaintIdentifierDTO", complaintIdentifierDTO);
        return "redirect:/SRW/complaint/" + searchEndpointOrigin;
    }

    /*
     * Displays the complaint update form with the complaint's existing details.
     */
    @GetMapping("/update")
    public String updateComplaint(@RequestParam("complaintId") String complaintId, Model model, HttpSession session,
                                  HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String refererEndpoint = referer != null ? EndpointExtractor.complaintEndpoint(request) : "my-complaints";
        if (complaintService.canUserAccess(complaintId, UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER"),
                (String) session.getAttribute("userId"))) {
            ComplaintDTO complaint = complaintService.getComplaintById(complaintId);
            if(complaint == null) {
                ComplaintModel.addComplaintsToModel(Map.of(), new ComplaintIdentifierDTO(complaintId, null),
                        List.of(), "No complaints found.", model);
                return "complaint/complaint-list";
            }
            ComplaintModel.addComplaintToModel(complaint, model);
            ComplaintModel.populateComplaintUpdate(complaintService.getActiveTechnicians(),
                    refererEndpoint.equals("search") ? (String) session.getAttribute("searchEndpointOrigin") : refererEndpoint, model);
            return "complaint/complaint-update-form";
        }
        return "access-denied";
    }

    /*
     * Updates a complaint after validating the input.
     * Redirects to the appropriate origin complaint page.
     */
    @PostMapping("/update")
    public String updateComplaint(@ModelAttribute("complaint") @Valid ComplaintDTO updatedComplaintDTO, BindingResult bindingResult,
                                  @RequestParam("updateEndpointOrigin") String updateEndpointOrigin, Model model) {
        if (bindingResult.hasErrors()) {
            if (updatedComplaintDTO.getProductType() != null) {
                ComplaintModel.populateDropDownsForProduct(updatedComplaintDTO.getProductType(), model);
            } else {
                ComplaintModel.addProductTypesToModel(model);
            }
            ComplaintModel.populateComplaintUpdate(complaintService.getActiveTechnicians(), updateEndpointOrigin, model);
            return "complaint/complaint-update-form";
        }
        complaintService.updateComplaint(complaintService.getComplaintById(updatedComplaintDTO.getComplaintId()), updatedComplaintDTO);
        return "redirect:/SRW/complaint/" + updateEndpointOrigin;
    }

    /*
     * Activates a complaint based on the provided complaintId.
     */
    @GetMapping("/activate")
    public String activateComplaint(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        complaintService.activateComplaint(complaintId);
        return "redirect:/SRW/complaint/" + EndpointExtractor.complaintEndpoint(request);
    }

    /*
     * Deactivates a complaint based on the provided complaintId.
     */
    @GetMapping("/deactivate")
    public String deactivateComplaint(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        complaintService.deactivateComplaint(complaintId);
        return "redirect:/SRW/complaint/" + EndpointExtractor.complaintEndpoint(request);
    }

    /*
     * Displays complaints assigned to the logged-in technician.
     */
    @GetMapping("/assigned-complaints")
    public String getAssignedComplaints(Model model, HttpSession session) {
        ComplaintIdentifierDTO complaintIdentifierDTO = (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO");
        ComplaintModel.addComplaintsToModel(complaintService.getActiveTechniciansInfo(),
                complaintIdentifierDTO == null ? new ComplaintIdentifierDTO() : complaintIdentifierDTO,
                complaintService.getComplaintsByTechnicianId((String) session.getAttribute("userId")),
                "No complaints have been assigned yet.", model);
        return "complaint/complaint-list";
    }
}