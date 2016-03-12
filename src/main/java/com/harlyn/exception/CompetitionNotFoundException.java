package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class CompetitionNotFoundException extends RuntimeException {

	public CompetitionNotFoundException(Long competitionId) {
		super("Competition with id " + competitionId + " not found");
	}
}
