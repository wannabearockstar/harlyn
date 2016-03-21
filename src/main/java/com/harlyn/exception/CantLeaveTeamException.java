package com.harlyn.exception;

/**
 * Created by wannabe on 21.03.16.
 */
public class CantLeaveTeamException extends RuntimeException {

	public CantLeaveTeamException() {
		super("You cant leave this team in according state");
	}
}
