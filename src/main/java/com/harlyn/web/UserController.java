package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.MissingUserExcpetion;
import com.harlyn.exception.UserNotFoundException;
import com.harlyn.service.TeamService;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public String meAction(Model model) {
        model.addAttribute("me", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/me";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String userAction(Model model, @PathVariable(value = "id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        model.addAttribute(user);
        model.addAttribute("me", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/show";
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteAction(@PathVariable(value = "id") Long userId) {
        User me = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getById(userId);

        if (user == null) {
            throw new MissingUserExcpetion();
        }
        teamService.sendInvite(me, user);
        eventPublisher.publishEvent(new UserChangedEvent(this));
        return "redirect:/users/" + userId;
    }
}
