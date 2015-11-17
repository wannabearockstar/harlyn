package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.event.UserCreatedEvent;
import com.harlyn.exception.NonUniqueUserDataException;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;

/**
 * Created by wannabe on 15.11.15.
 */
@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/login/form", method = RequestMethod.GET)
    public String loginForm(Model model) {
        return "security/login";
    }

    @RequestMapping(value = "/registration/form", method = RequestMethod.GET)
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "security/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("user", user);
            return "security/registration";
        }
        userService.validateUniqueData(user);
        User userCreated = userService.createUser(user);
        eventPublisher.publishEvent(new UserCreatedEvent(this, userCreated));
        return "redirect:/login/form?register";
    }

    @ExceptionHandler(NonUniqueUserDataException.class)
    public ModelAndView nonUniqueUserDataException(NonUniqueUserDataException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errors", Collections.singleton(new ObjectError("User", e.getMessage())));
        mav.addObject("user", e.getUser());
        mav.setViewName("security/registration");
        return mav;
    }
}
