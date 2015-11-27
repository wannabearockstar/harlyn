package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.*;
import com.harlyn.service.TeamService;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        return "user/me";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String userAction(Model model, @PathVariable(value = "id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        model.addAttribute(user);
        return "user/show";
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteAction(@PathVariable(value = "id") Long userId, Model model) {
        User user = userService.getById(userId);

        if (user == null) {
            throw new MissingUserExcpetion();
        }
        teamService.sendInvite((User) model.asMap().get("me"), user);
        eventPublisher.publishEvent(new UserChangedEvent(this));
        return "redirect:/users/" + userId;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MissingUserExcpetion.class)
    public ModelAndView TeamNotFoundException(TeamNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MissingUserExcpetion.class)
    public ModelAndView UserNotFoundException(UserNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserWithoutTeamException.class)
    public ModelAndView UserWithoutTeamException(UserWithoutTeamException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserInvalidTeamRightsException.class)
    public ModelAndView UserInvalidTeamRightsException(UserInvalidTeamRightsException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", e.getMessage());
        mav.setViewName("utils/errors/default");
        return mav;
    }
}
