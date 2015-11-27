package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by wannabe on 27.11.15.
 */
@ControllerAdvice(basePackages = "com.harlyn.web")
public class LoadGlobalData {
    @Autowired
    private UserService userService;

    @ModelAttribute("me")
    public User getCurrentUser() {
        return userService.getLoggedUser();
    }
}
