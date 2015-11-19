package com.harlyn.web;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.TeamNotFoundException;
import com.harlyn.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wannabe on 18.11.15.
 */
@Controller
@RequestMapping(value = "/team")
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createTeam(@RequestParam(value = "name", required = true) String name) {
        Team team = teamService.createTeam(name, (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "redirect:/team/" + team.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showTeam(@PathVariable(value = "id") Long teamId, Model model) {
        Team team = teamService.getById(teamId);
        if (team == null) {
            throw new TeamNotFoundException();
        }
        model.addAttribute("team", team);
        return "team/show";
    }

    @RequestMapping(value = "/{id}/invite/accept", method = RequestMethod.POST)
    public String acceptInvite(@PathVariable(value = "id") Long teamId) {
        Team team = teamService.getById(teamId);
        if (team == null) {
            throw new TeamNotFoundException();
        }
        teamService.confirmInvite((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), team);
        eventPublisher.publishEvent(new UserChangedEvent(this));

        return "redirect:/team/" + teamId;
    }
}
