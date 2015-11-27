package com.harlyn.listener;

import com.harlyn.event.UserChangedEvent;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
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
            //Reload memory cache for user
            userService.bust(((UserChangedEvent) event).getUserId());
        }
    }
}
