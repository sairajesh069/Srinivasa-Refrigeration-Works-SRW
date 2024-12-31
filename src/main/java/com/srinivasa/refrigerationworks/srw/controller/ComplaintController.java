package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.ComplaintModel;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintDTO;
import com.srinivasa.refrigerationworks.srw.payload.dto.ComplaintIdentifierDTO;
import com.srinivasa.refrigerationworks.srw.service.ComplaintService;
import com.srinivasa.refrigerationworks.srw.utility.UserRoleProvider;
import com.srinivasa.refrigerationworks.srw.utility.common.StringEditor;
import com.srinivasa.refrigerationworks.srw.utility.common.SubStringExtractor;
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
        complaintService.registerComplaint(complaintDTO, session.getAttribute("userId").toString());
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
                    complaintService.getActiveEmployeeIds(), request.getParameter("updateEndpointOrigin"), model);
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
        ComplaintModel.addComplaintsToModel(model.getAttribute("complaintIdentifierDTO") == null
                        ? new ComplaintIdentifierDTO() : (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO"),
                complaintService.getComplaintsByBookedById(session.getAttribute("userId").toString()),
                "No complaints have been registered yet.", model);
        return "complaint/complaint-list";
    }

    /*
     * Displays the list of all complaints.
     */
    @GetMapping("/list")
    public String getComplaintList(Model model) {
        ComplaintModel.addComplaintsToModel(model.getAttribute("complaintIdentifierDTO") == null
                        ? new ComplaintIdentifierDTO() : (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO"),
                complaintService.getComplaintList(), "No complaints have been registered yet.", model);
        return "complaint/complaint-list";
    }

    /*
     * Displays the list of all active complaints.
     */
    @GetMapping("/active-list")
    public String getActiveComplaintList(Model model) {
        ComplaintModel.addComplaintsToModel(model.getAttribute("complaintIdentifierDTO") == null
                        ? new ComplaintIdentifierDTO() : (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO"),
                complaintService.getActiveComplaintList(), "No active complaints found.", model);
        return "complaint/complaint-list";
    }

    /*
     * Searches for complaints based on the identifier and displays results.
     */
    @PostMapping("/search")
    public String getComplaint(@ModelAttribute @Valid ComplaintIdentifierDTO complaintIdentifierDTO, BindingResult bindingResult,
                               HttpSession session, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String searchEndpointOrigin = SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/").equals("search")
                ? session.getAttribute("searchEndpointOrigin").toString()
                : SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/");
        if(!bindingResult.hasErrors() && complaintIdentifierDTO.getIdentifier() != null) {
            ComplaintModel.addComplaintsToModel(complaintIdentifierDTO,
                    complaintService.getComplaintByIdentifier(
                            complaintIdentifierDTO, session.getAttribute("userId").toString(), UserRoleProvider.fetchUserRole(session)
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
    public String updateComplaint(@RequestParam("complaintId") String complaintId, Model model,
                                  HttpSession session, HttpServletRequest request) {
        if (complaintService.canUserAccess(complaintId, UserRoleProvider.fetchUserRole(session).equals("ROLE_OWNER"),
                session.getAttribute("userId").toString())) {
            ComplaintModel.addComplaintToModel(complaintService.getComplaintById(complaintId), model);
            ComplaintModel.populateComplaintUpdate(complaintService.getActiveEmployeeIds(),
                    SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/").equals("search")
                            ? session.getAttribute("searchEndpointOrigin").toString()
                            : SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/"), model);
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
                                  @RequestParam("updateEndpointOrigin") String updateEndpointOrigin, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            if (updatedComplaintDTO.getProductType() != null) {
                ComplaintModel.populateDropDownsForProduct(updatedComplaintDTO.getProductType(), model);
            } else {
                ComplaintModel.addProductTypesToModel(model);
            }
            ComplaintModel.populateComplaintUpdate(complaintService.getActiveEmployeeIds(), updateEndpointOrigin, model);
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
        return "redirect:/SRW/complaint/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/");
    }

    /*
     * Deactivates a complaint based on the provided complaintId.
     */
    @GetMapping("/deactivate")
    public String deactivateComplaint(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        complaintService.deactivateComplaint(complaintId);
        return "redirect:/SRW/complaint/" + SubStringExtractor.extractSubString(request.getHeader("Referer"), "complaint/");
    }

    /*
     * Displays complaints assigned to the logged-in technician.
     */
    @GetMapping("/assigned-complaints")
    public String getAssignedComplaints(Model model, HttpSession session) {
        ComplaintModel.addComplaintsToModel(model.getAttribute("complaintIdentifierDTO") == null
                        ? new ComplaintIdentifierDTO() : (ComplaintIdentifierDTO) model.getAttribute("complaintIdentifierDTO"),
                complaintService.getComplaintsByTechnicianId(session.getAttribute("userId").toString()),
                "No complaints have been assigned yet.", model);
        return "complaint/complaint-list";
    }
}