package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.exception.MissingUserExcpetion;
import com.harlyn.service.TeamService;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 16.11.15.
 */
@Controller
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public String meAction(Model model) {
        model.addAttribute("me", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/me";
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteAction(@PathVariable(value = "id") Long userId) {
        User me = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getById(userId);

        if (user == null) {
            throw new MissingUserExcpetion();
        }
        teamService.sendInvite(me, user);
        return "redirect:/users/" + userId;
    }
}
