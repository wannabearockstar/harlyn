package com.harlyn.exception;

/**
 * Created by wannabe on 26.11.15.
 */
public class MissingSolutionException extends RuntimeException {

	public MissingSolutionException() {
		super("Missing solution");
	}
}
