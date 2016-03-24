package com.harlyn.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by wannabe on 24.03.16.
 */
public class MessagePurgedEvent extends ApplicationEvent {
	private final Long messageId;

	public MessagePurgedEvent(Object src, Long messageId) {
		super(src);
		this.messageId = messageId;
	}

	public Long getMessageId() {
		return messageId;
	}
}
