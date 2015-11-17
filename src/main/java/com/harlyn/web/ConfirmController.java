package com.harlyn.web;

import com.harlyn.exception.InvalidConfirmCodeException;
import com.harlyn.service.ConfirmCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wannabe on 16.11.15.
 */
@Controller
@RequestMapping("/confirm")
public class ConfirmController {
    @Autowired
    private ConfirmCodeService confirmCodeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String confirmUser(@RequestParam(value ="code", required = true) String code) {
        confirmCodeService.confirmUserByCode(code);
        return "redirect:/confirm/final?success";
    }

    @RequestMapping(value = "/final", method = RequestMethod.GET)
    public String confirmPageSuccess() {
        return "user/confirm";
    }

    @ExceptionHandler(InvalidConfirmCodeException.class)
    public String invalidConfirmCodeException(InvalidConfirmCodeException e) {
        return "redirect:/confirm/final?error";
    }
}
