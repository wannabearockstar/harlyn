package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class UserAlreadyInTeamException extends RuntimeException {

	public UserAlreadyInTeamException() {
		super("User already in team");
	}
}
