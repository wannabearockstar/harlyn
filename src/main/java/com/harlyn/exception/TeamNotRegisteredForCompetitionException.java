package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class TeamNotRegisteredForCompetitionException extends RuntimeException {

	public TeamNotRegisteredForCompetitionException() {
		super("Team not register for corresponding competition");
	}
}
