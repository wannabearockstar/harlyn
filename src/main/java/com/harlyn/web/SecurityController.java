package com.harlyn.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 15.11.15.
 */
@Controller
public class SecurityController {
    @RequestMapping(value = "/login/form", method = RequestMethod.GET)
    public String loginForm(Model model) {
        return "login";
    }
}
