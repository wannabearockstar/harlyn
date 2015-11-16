package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by wannabe on 15.11.15.
 */
@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

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
        userService.createUser(user);
        return "redirect:/login/form?register";
    }
}
