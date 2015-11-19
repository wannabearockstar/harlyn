package com.harlyn.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 19.11.15.
 */
public class UserChangedEvent extends ApplicationEvent {

    public UserChangedEvent(Object source) {
        super(source);
    }
}
