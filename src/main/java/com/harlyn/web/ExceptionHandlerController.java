package com.harlyn.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by wannabe on 28.11.15.
 */
@ControllerAdvice
public class ExceptionHandlerController {
    public static final String DEFAULT_ERROR_VIEW = "utils/error/default";

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ModelAndView defaultErrorHandler(Exception e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("message", e.getMessage());
        return mav;
    }
}
