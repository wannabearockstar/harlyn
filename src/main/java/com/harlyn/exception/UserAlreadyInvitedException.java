package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class UserAlreadyInvitedException extends RuntimeException {

	public UserAlreadyInvitedException() {
		super("User already invited");
	}
}
