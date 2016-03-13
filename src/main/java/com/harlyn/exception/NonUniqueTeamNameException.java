package com.harlyn.exception;

/**
 * Created by wannabe on 18.11.15.
 */
public class NonUniqueTeamNameException extends RuntimeException {

	public NonUniqueTeamNameException(String name) {
		super("Team name " + name + " already taken.");
	}
}
