package com.harlyn.exception;

/**
 * Created by wannabe on 19.11.15.
 */
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("User not found");
	}
}
