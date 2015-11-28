package com.harlyn.web;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.NonUniqueTeamNameException;
import com.harlyn.exception.TeamNotFoundException;
import com.harlyn.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public String createTeam(@RequestParam(value = "name", required = true) String name, Model model) {
        Team team = teamService.createTeam(name, (User) model.asMap().get("me"));
        eventPublisher.publishEvent(new UserChangedEvent(this, (User) model.asMap().get("me")));
        return "redirect:/team/" + team.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showTeam(@PathVariable(value = "id") Long teamId, Model model) {
        Team team = teamService.getById(teamId);
        if (team == null) {
            throw new TeamNotFoundException(teamId);
        }
        model.addAttribute("team", team);
        return "team/show";
    }

    @RequestMapping(value = "/{id}/invite/accept", method = RequestMethod.POST)
    public String acceptInvite(@PathVariable(value = "id") Long teamId, Model model) {
        Team team = teamService.getById(teamId);
        if (team == null) {
            throw new TeamNotFoundException(teamId);
        }
        teamService.confirmInvite((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), team);
        eventPublisher.publishEvent(new UserChangedEvent(this, (User) model.asMap().get("me")));

        return "redirect:/team/" + teamId;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String teamsListPage(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        return "team/list";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TeamNotFoundException.class)
    public ModelAndView teamNotFoundException(TeamNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NonUniqueTeamNameException.class)
    public ModelAndView nonUniqueTeamNameException(NonUniqueTeamNameException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }
}
