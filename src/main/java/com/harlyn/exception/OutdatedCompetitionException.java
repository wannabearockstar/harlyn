package com.harlyn.exception;

/**
 * Created by wannabe on 02.12.15.
 */
public class OutdatedCompetitionException extends RuntimeException {

	public OutdatedCompetitionException() {
		super("Competition no longer available for participating");
	}
}
