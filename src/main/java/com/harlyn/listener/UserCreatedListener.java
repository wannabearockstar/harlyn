package com.harlyn.listener;

import com.harlyn.domain.User;
import com.harlyn.event.UserCreatedEvent;
import com.harlyn.service.ConfirmCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by wannabe on 16.11.15.
 */
@Component
public class UserCreatedListener implements ApplicationListener {

	@Autowired
	private ConfirmCodeService confirmCodeService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof UserCreatedEvent) {
			User user = ((UserCreatedEvent) event).getUser();
			confirmCodeService.createConfirmCode(user);
		}
	}
}
