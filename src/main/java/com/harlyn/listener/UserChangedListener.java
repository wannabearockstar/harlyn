package com.harlyn.listener;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by wannabe on 19.11.15.
 */
@Component
public class UserChangedListener implements ApplicationListener {
    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserChangedEvent && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User newUser = userService.getById(currentUser.getId());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(newUser, newUser.getUsername(), newUser.getAuthorities()));
        }
    }
}
