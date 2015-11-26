package com.harlyn.web.admin;

import com.harlyn.domain.User;
import com.harlyn.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/team")
public class AdminTeamController {
    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listTeamPage(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("me", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "admin/team/list";
    }
}
