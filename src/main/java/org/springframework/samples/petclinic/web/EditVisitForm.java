package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Visit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * JavaBean Form controller that is used to delete an existing <code>Visit</code>.
 *
 * @author Copilot
 */
@Controller
@RequestMapping("/owners/*/pets/*/visits/{visitId}/edit")
public class EditVisitForm {

    private final Clinic clinic;

    @Autowired
    public EditVisitForm(Clinic clinic) {
        this.clinic = clinic;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteVisit(@PathVariable int visitId) {
        Visit visit = this.clinic.loadVisit(visitId);
        int ownerId = visit.getPet().getOwner().getId();
        this.clinic.deleteVisit(visitId);
        return "redirect:/owners/" + ownerId;
    }
}
