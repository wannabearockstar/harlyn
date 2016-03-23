package com.harlyn.exception;

/**
 * Created by wannabe on 23.03.16.
 */
public class ChatMessageNotFoundException extends RuntimeException {

	public ChatMessageNotFoundException() {
		super("Chat message not found");
	}
}
