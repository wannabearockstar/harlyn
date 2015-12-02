package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;
import com.harlyn.exception.CompetitionNotFoundException;
import com.harlyn.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 02.12.15.
 */
@Controller
@RequestMapping(value = "/competition")
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String competitionPage(@PathVariable(value = "id") Long id, Model model) {
        Competition competition = competitionService.findById(id);
        if (competition == null) {
            throw new CompetitionNotFoundException(id);
        }
        model.addAttribute("competition", competition);
        return "competition/show";
    }

    @RequestMapping(value = "/{id}/register", method = RequestMethod.POST)
    public String registerTeamForCompetition(@PathVariable(value = "id") Long id, Model model) {
        Competition competition = competitionService.findById(id);
        if (competition == null) {
            throw new CompetitionNotFoundException(id);
        }
        competitionService.registerUserTeamToCompetition(competition, (User) model.asMap().get("me"));
        return "redirect:/competition/" + id;
    }
}
