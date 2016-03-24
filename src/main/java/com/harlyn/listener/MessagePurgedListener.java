package com.harlyn.listener;

import com.harlyn.event.MessagePurgedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by wannabe on 24.03.16.
 */
@Component
public class MessagePurgedListener implements ApplicationListener<MessagePurgedEvent> {

	@Autowired
	private SimpMessagingTemplate template;

	@Override
	public void onApplicationEvent(MessagePurgedEvent event) {
		// TODO: 24.03.16 сделать нормальный резолв ендпоинта
		template.convertAndSend("/out/competition.purge", event.getMessageId());
	}
}
