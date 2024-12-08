package com.srinivasa.refrigerationworks.srw.controller;

import com.srinivasa.refrigerationworks.srw.model.OwnerModel;
import com.srinivasa.refrigerationworks.srw.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller that handles requests related to the Owner entity.
 * It provides a mapping to fetch and display the list of owners in the owner-list view.
 */
@Controller
@RequestMapping("/SRW/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    /*
     * Handles GET requests to fetch the list of all owners.
     * Adds the list of owners to the model and returns the view for displaying owner details.
     */
    @GetMapping("/list")
    public String getOwnerList(Model model) {
        OwnerModel.addOwnerListToModel(ownerService.getOwnerList(), model);
        return "owner/owner-list";
    }
}
