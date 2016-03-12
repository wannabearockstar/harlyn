package com.harlyn.event;

import com.harlyn.domain.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 16.11.15.
 */
public class UserCreatedEvent extends ApplicationEvent {

	private final User user;

	public UserCreatedEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
