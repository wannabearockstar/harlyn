package com.harlyn.config;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by wannabe on 15.11.15.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email " + email + " not found");
        }
        eventPublisher.publishEvent(new UserChangedEvent(this, user));
        return user;
    }
}
