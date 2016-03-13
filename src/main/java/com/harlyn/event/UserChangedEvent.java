package com.harlyn.event;

import com.harlyn.domain.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 19.11.15.
 */
public class UserChangedEvent extends ApplicationEvent {

	private final User user;

	public UserChangedEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

	public long getUserId() {
		return user.getId();
	}
}
