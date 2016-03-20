package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class UserInvalidTeamRightsException extends RuntimeException {

	public UserInvalidTeamRightsException() {
		super("User has invalid team rights");
	}
}
