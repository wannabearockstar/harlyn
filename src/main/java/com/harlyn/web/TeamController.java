package com.harlyn.web;

import com.harlyn.domain.Team;
import com.harlyn.domain.User;
import com.harlyn.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
    TeamService teamService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createTeam(@RequestParam(value = "name", required = true) String name) {
        Team team = teamService.createTeam(name, (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "redirect:/team/" + team.getId();
    }
}
