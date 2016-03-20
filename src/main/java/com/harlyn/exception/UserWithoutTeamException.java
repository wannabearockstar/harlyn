package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class UserWithoutTeamException extends RuntimeException {

	public UserWithoutTeamException() {
		super("User without team");
	}
}
