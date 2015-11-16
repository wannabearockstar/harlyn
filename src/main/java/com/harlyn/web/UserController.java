package com.harlyn.web;

import com.harlyn.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wannabe on 16.11.15.
 */
@Controller
@RequestMapping(value = "/users")
public class UserController {
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public String meAction(Model model) {
        model.addAttribute("me", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/me";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmUser(@RequestParam(value ="code", required = true) String code) {
        //todo confirm user and show template with message. change route of mail
    }
}
