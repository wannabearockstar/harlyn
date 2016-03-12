package com.harlyn.event;

import com.harlyn.domain.chat.ChatMessage;
import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 14.12.15.
 */
public class MessagePublishedEvent extends ApplicationEvent {

	private final ChatMessage chatMessage;

	public MessagePublishedEvent(Object source, ChatMessage chatMessage) {
		super(source);
		this.chatMessage = chatMessage;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}
}
